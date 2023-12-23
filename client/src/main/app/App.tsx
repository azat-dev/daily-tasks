import React, { useEffect } from "react";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AppRouter from "../../presentation/router";
import AppModelImpl from "./model/AppModelImpl";
import useUpdatesFrom from "../../presentation/utils/useUpdatesFrom";
import ModalPresenter from "./ModalPresenter";
import AppSettings from "./AppSettings";
import AppModel from "./model/AppModel";
import useAppPages from "./useAppPages";
import AuthTokensRepositoryImpl from "../../data/repositories/AuthTokensRepositoryImpl";
import AuthStateRepositoryImpl from "../../data/repositories/AuthStateRepositoryImpl";
import StartNewSessionUseCaseImpl from "../../domain/usecases/auth/StartNewSessionUseCase/StartNewSessionUseCaseImpl";
import { Configuration, DefaultApi } from "../../data/API";
import AuthenticationServiceImpl from "../../data/services/AuthenticationServiceImpl";
import ListenAuthenticationStateUseCaseImpl from "../../domain/usecases/auth/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCaseImpl";
import AuthState from "../../domain/models/auth/AuthState";
import LogInByUserNameAndPasswordUseCaseImpl from "../../domain/usecases/auth/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCaseImpl";
import LogInPageViewModelImpl from "../../presentation/pages/LogInPage/ViewModel/LogInPageViewModelImpl";
import { Result, ResultType } from "../../common/Result";
import EditTaskModalViewModelImpl from "../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModelImpl";
import TasksRepositoryImpl from "../../data/repositories/TasksRepositoryImpl";
import LoadTaskUseCaseImpl from "../../domain/usecases/LoadTaskUseCase/LoadTaskUseCaseImpl";
import { TaskId } from "../../domain/models/Task";
import EditTaskUseCaseImpl from "../../domain/usecases/EditTaskUseCase/AddNewTaskUseCaseImpl";
import DayPageViewViewModel, {
    DayPageViewViewModelDelegate,
} from "../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import DeleteTaskUseCaseImpl from "../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import StopTaskUseCaseImpl from "../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import StartTaskUseCaseImpl from "../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import TaskMapperImpl from "../../data/repositories/TaskMapper/TaskMapperImpl";
import StartTaskUseCase from "../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StopTaskUseCase from "../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import DeleteTaskUseCase from "../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import BacklogType from "../../domain/models/BacklogType";
import DayPageViewModelImpl from "../../presentation/pages/DayPage/ViewModel/DayPageViewModelImpl";
import ListTasksInBacklogUseCase from "../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import ListTasksInBacklogUseCaseImpl from "../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import AddTaskViewModelImpl from "../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModelImpl";
import AddNewTaskUseCaseImpl from "../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";

const AppRoot = (props: { settings: AppSettings }) => {
    const { settings } = props;

    const localTokensRepository = new AuthTokensRepositoryImpl();
    const authStateRepository = new AuthStateRepositoryImpl();

    const apiClient = new DefaultApi(
        new Configuration({
            basePath: settings.api.basePath,
            accessToken: async () => {
                const tokens = await localTokensRepository.getTokens();
                return tokens?.accessToken ?? "";
            },
        })
    );

    const authService = new AuthenticationServiceImpl(apiClient);

    const startNewSession = () => {
        const startNewSessionUseCase = new StartNewSessionUseCaseImpl(
            authService,
            localTokensRepository,
            authStateRepository
        );

        startNewSessionUseCase.startNewSession();
    };

    const startListeningAuthStateChanges = (
        listener: (authState: AuthState) => void
    ) => {
        const listenAuthStateUseCase = new ListenAuthenticationStateUseCaseImpl(
            authStateRepository
        );
        const subscripbtion = listenAuthStateUseCase.listen(listener);
    };

    const makeLoginPageModel = () => {
        return new LogInPageViewModelImpl(async (username, password) => {
            const logInUseCase = new LogInByUserNameAndPasswordUseCaseImpl(
                authService,
                localTokensRepository,
                authStateRepository
            );

            const result = await logInUseCase.execute(username, password);

            return result.type === ResultType.Success;
        });
    };

    const getTasksRepository = () => {
        return new TasksRepositoryImpl(apiClient, new TaskMapperImpl());
    };

    const loadTaskUseCaseFactory = () => {
        return new LoadTaskUseCaseImpl(getTasksRepository());
    };

    const editTaskUseCaseFactory = () => {
        return new EditTaskUseCaseImpl(getTasksRepository());
    };

    const makeEditTaskModalModel = (
        taskId: TaskId,
        delegate: {
            hideModal: () => void;
            hideModalAfterCreation: () => void;
        }
    ) => {
        const vm = new EditTaskModalViewModelImpl(taskId, async () => {
            const useCase = loadTaskUseCaseFactory();
            const result = await useCase.execute(taskId);
            return Result.mapError(result, () => undefined);
        });

        vm.delegate = {
            updateTask: async (taskId, task) => {
                const useCase = editTaskUseCaseFactory();
                const result = await useCase.execute(taskId, task);

                return Result.mapError(result, () => undefined);
            },
            didComplete: delegate.hideModalAfterCreation,
            didHide: delegate.hideModal,
        };

        return vm;
    };

    const getStartTaskUseCase = (): StartTaskUseCase => {
        return new StartTaskUseCaseImpl(getTasksRepository());
    };

    const getStopTaskUseCase = (): StopTaskUseCase => {
        return new StopTaskUseCaseImpl(getTasksRepository());
    };

    const getDeleteTaskUseCase = (): DeleteTaskUseCase => {
        return new DeleteTaskUseCaseImpl(getTasksRepository());
    };

    const getListTasksInBacklogUseCase = (
        backlogType: BacklogType
    ): ListTasksInBacklogUseCase => {
        return new ListTasksInBacklogUseCaseImpl(
            backlogType,
            getTasksRepository()
        );
    };

    const makeBacklogDayPageViewModel = (
        backlogDay: string,
        delegate: {
            runAddTaskFlow: (
                backlogType: BacklogType,
                backlogDay: string,
                didAdd: () => void
            ) => void;
            runEditTaskFlow: (
                taskId: TaskId,
                didUpdateTask: () => void
            ) => void;
        }
    ): DayPageViewViewModel => {
        const backlogType = BacklogType.Day;

        const vm = new DayPageViewModelImpl();

        const vmDelegate: DayPageViewViewModelDelegate = {
            loadTasks: async () => {
                const useCase = getListTasksInBacklogUseCase(backlogType);
                const result = await useCase.execute(backlogDay);
                return Result.mapError(result, () => undefined);
            },
            loadStatuses: async () => {
                return Result.success({});
            },
            startTask: async (taskId) => {
                const result = await getStartTaskUseCase().execute(taskId);
                return Result.mapError(result, () => undefined);
            },
            stopTask: async (taskId) => {
                const result = await getStopTaskUseCase().execute(taskId);
                return Result.mapError(result, () => undefined);
            },
            deleteTask: async (taskId) => {
                const result = await getDeleteTaskUseCase().execute(taskId);
                return Result.mapError(result, () => undefined);
            },
            runAddTaskFlow: () => {
                delegate.runAddTaskFlow(backlogType, backlogDay, () => {
                    vm.reloadTasks(true);
                });
            },
            openTask: (taskId) => {
                delegate.runEditTaskFlow(taskId, () => vm.reloadTasks(true));
            },
        };

        vm.delegate = vmDelegate;

        return vm;
    };

    const getAddNewTaskUseCase = () => {
        return new AddNewTaskUseCaseImpl(getTasksRepository());
    };

    const makeAddTaskModalViewModel = (
        backlogType: BacklogType,
        backlogDay: string,
        delegate: {
            didComplete: () => void;
            didHide: () => void;
        }
    ) => {
        return new AddTaskViewModelImpl({
            createTask: async (newTask): Promise<Result<TaskId, undefined>> => {
                const addNewTaskUseCase = getAddNewTaskUseCase();

                const result = await addNewTaskUseCase.execute(
                    backlogType,
                    backlogDay,
                    newTask
                );

                if (result.type === ResultType.Success) {
                    return Result.success(result.value);
                }

                return Result.failure(undefined);
            },
            didComplete: delegate.didComplete,
            didHide: delegate.didHide,
        });
    };

    const model = new AppModelImpl(
        startNewSession,
        startListeningAuthStateChanges,
        makeLoginPageModel,
        makeEditTaskModalModel,
        makeBacklogDayPageViewModel,
        makeAddTaskModalViewModel
    );

    (window as any).appModel = model;

    useEffect(() => {
        model.start();
    }, [model]);
    return <AppView model={model} />;
};

const AppView = ({ model }: { model: AppModel }) => {
    useUpdatesFrom(model.authState, model.currentModal);
    const pages = useAppPages(model.getPages);

    return (
        <div className="appRoot">
            <AppRouter
                authState={model.authState.value}
                signInPagePath="/sign-in"
                pages={pages}
            />
            <ModalPresenter currentModal={model.currentModal.value} />
        </div>
    );
};

export default AppRoot;
