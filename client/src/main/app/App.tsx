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

            const result = await logInUseCase.logInByUserName(
                username,
                password
            );

            return result.type === ResultType.Success;
        });
    };

    const tasksRepository = new TasksRepositoryImpl(apiClient);

    const loadTaskUseCaseFactory = () => {
        return new LoadTaskUseCaseImpl(tasksRepository);
    };

    const editTaskUseCaseFactory = () => {
        return new EditTaskUseCaseImpl(tasksRepository);
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

    const model = new AppModelImpl(
        apiClient,
        startNewSession,
        startListeningAuthStateChanges,
        makeLoginPageModel,
        makeEditTaskModalModel
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
