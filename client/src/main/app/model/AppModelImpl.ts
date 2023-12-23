import { Result, ResultType } from "../../../common/Result";
import { Configuration, DefaultApi } from "../../../data/API";
import AuthStateRepositoryImpl from "../../../data/repositories/AuthStateRepositoryImpl";
import TaskMapperImpl from "../../../data/repositories/TaskMapper/TaskMapperImpl";
import TasksRepositoryImpl from "../../../data/repositories/TasksRepositoryImpl";
import AuthenticationServiceImpl from "../../../data/services/AuthenticationServiceImpl";
import {
    AuthenticationServiceByUserNameAndPassword,
    AuthenticationServiceCheckToken,
} from "../../../domain/interfaces/services/AuthenticationService";
import AuthState from "../../../domain/models/auth/AuthState";
import BacklogType from "../../../domain/models/BacklogType";
import { TaskId } from "../../../domain/models/Task";
import AddNewTaskUseCaseImpl from "../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import { ListenAuthenticationStateUseCaseOutput } from "../../../domain/usecases/auth/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCase";
import ListenAuthenticationStateUseCaseImpl from "../../../domain/usecases/auth/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCaseImpl";
import LogInByUserNameAndPasswordUseCaseImpl from "../../../domain/usecases/auth/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCaseImpl";
import StartNewSessionUseCaseImpl from "../../../domain/usecases/auth/StartNewSessionUseCase/StartNewSessionUseCaseImpl";
import DeleteTaskUseCase from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import DeleteTaskUseCaseImpl from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import EditTaskUseCaseImpl from "../../../domain/usecases/EditTaskUseCase/AddNewTaskUseCaseImpl";
import EditTaskUseCase from "../../../domain/usecases/EditTaskUseCase/EditTaskUseCase";
import ListTasksInBacklogUseCase from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import ListTasksInBacklogUseCaseImpl from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import { LoadTaskUseCase } from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCase";
import LoadTaskUseCaseImpl from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCaseImpl";
import StartTaskUseCase from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StartTaskUseCaseImpl from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCase from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import StopTaskUseCaseImpl from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import AddTaskViewModelImpl from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModelImpl";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import EditTaskModalViewModelImpl from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModelImpl";
import DayPageViewViewModel, {
    DayPageViewViewModelDelegate as DayPageViewModelDelegate,
} from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import DayPageViewModelImpl from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModelImpl";
import LogInPageViewModel from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import LogInPageViewModelImpl from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModelImpl";
import Subject from "../../../presentation/utils/Subject";
import value from "../../../presentation/utils/value";
import AppSettings from "../AppSettings";
import AppModel, { AppModelPageFactories, CurrentModalState } from "./AppModel";
import { AuthStateRepository } from "../../../domain/repositories/AuthStateRepository";
import AuthTokensRepository from "../../../domain/repositories/AuthTokensRepository";

export default class AppModelImpl implements AppModel {
    // Properties

    public currentModal: Subject<CurrentModalState | null> = value(null);
    public authState: Subject<AuthState> = value(AuthState.PROCESSING);

    private apiClient: DefaultApi;
    private authService: AuthenticationServiceByUserNameAndPassword &
        AuthenticationServiceCheckToken;
    private listenAuthStateUseCase: ListenAuthenticationStateUseCaseOutput;

    // Constructor

    public constructor(
        settings: AppSettings,
        private readonly localTokensRepository: AuthTokensRepository,
        private readonly authStateRepository: AuthStateRepository
    ) {
        this.apiClient = new DefaultApi(
            new Configuration({
                basePath: settings.api.basePath,
                accessToken: async () => {
                    const tokens = await this.localTokensRepository.getTokens();
                    return tokens?.accessToken ?? "";
                },
            })
        );

        this.authService = new AuthenticationServiceImpl(this.apiClient);

        this.listenAuthStateUseCase = new ListenAuthenticationStateUseCaseImpl(
            this.authStateRepository
        );
    }

    // Methods

    private runAddTaskFlow = (
        backlogType: BacklogType,
        backlogDay: string,
        didAdd: () => void
    ) => {
        const addNewTaskUseCase = new AddNewTaskUseCaseImpl(
            this.getTasksRepository()
        );

        this.currentModal.set({
            type: "addTask",
            viewModel: new AddTaskViewModelImpl({
                createTask: async (
                    newTask
                ): Promise<Result<TaskId, undefined>> => {
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
                didComplete: () => {
                    this.currentModal.set(null);
                    didAdd();
                },
                didHide: () => {
                    this.currentModal.set(null);
                },
            }),
        });
    };

    private getTasksRepository = () => {
        return new TasksRepositoryImpl(this.apiClient, new TaskMapperImpl());
    };

    private getListTasksInBacklogUseCase = (
        backlogType: BacklogType
    ): ListTasksInBacklogUseCase => {
        return new ListTasksInBacklogUseCaseImpl(
            backlogType,
            this.getTasksRepository()
        );
    };

    private getStartTaskUseCase = (): StartTaskUseCase => {
        const tasksRepository = this.getTasksRepository();
        return new StartTaskUseCaseImpl(tasksRepository);
    };

    private getStopTaskUseCase = (): StopTaskUseCase => {
        const tasksRepository = this.getTasksRepository();
        return new StopTaskUseCaseImpl(tasksRepository);
    };

    private getDeleteTaskUseCase = (): DeleteTaskUseCase => {
        return new DeleteTaskUseCaseImpl(this.getTasksRepository());
    };

    private getEditTaskUseCase = (): EditTaskUseCase => {
        return new EditTaskUseCaseImpl(this.getTasksRepository());
    };

    public getPages = (): AppModelPageFactories => {
        return {
            makeLogInPageViewModel: this.makeLogInPageViewModel,
            makeBacklogDayPageViewModel: this.makeBacklogDayPageViewModel,
        };
    };

    private makeLogInPageViewModel = (): LogInPageViewModel => {
        const logInUseCase = new LogInByUserNameAndPasswordUseCaseImpl(
            this.authService,
            this.localTokensRepository,
            this.authStateRepository
        );

        return new LogInPageViewModelImpl(logInUseCase);
    };

    private getLoadTaskUseCase = (): LoadTaskUseCase => {
        return new LoadTaskUseCaseImpl(this.getTasksRepository());
    };

    private makeEditTaskPageViewModel = (
        taskId: TaskId,
        hideModal: () => void,
        hideModalAfterCreation: () => void,
        editTaskUseCaseFactory: () => EditTaskUseCase
    ): EditTaskModalViewModel => {
        const vm = new EditTaskModalViewModelImpl(taskId, async () => {
            const useCase = this.getLoadTaskUseCase();
            const result = await useCase.execute(taskId);
            return Result.mapError(result, () => undefined);
        });

        vm.delegate = {
            updateTask: async (taskId, task) => {
                const useCase = editTaskUseCaseFactory();
                const result = await useCase.execute(taskId, task);

                return Result.mapError(result, () => undefined);
            },
            didComplete: hideModalAfterCreation,
            didHide: hideModal,
        };

        return vm;
    };

    public runEditTaskFlow = (
        taskId: TaskId,
        didCompleteUpdate: () => void
    ) => {
        const vm = this.makeEditTaskPageViewModel(
            taskId,
            () => this.currentModal.set(null),
            () => {
                didCompleteUpdate();
                this.currentModal.set(null);
            },
            () => this.getEditTaskUseCase()
        );

        this.currentModal.set({
            type: "editTask",
            viewModel: vm,
        });
    };

    private makeBacklogDayPageViewModel = (
        backlogDay: string
    ): DayPageViewViewModel => {
        const backlogType = BacklogType.Day;

        const vm = new DayPageViewModelImpl();

        const delegate: DayPageViewModelDelegate = {
            loadTasks: async () => {
                const useCase = this.getListTasksInBacklogUseCase(backlogType);
                const result = await useCase.execute(backlogDay);
                return Result.mapError(result, () => undefined);
            },
            loadStatuses: async () => {
                return Result.success({});
            },
            startTask: async (taskId) => {
                const result = await this.getStartTaskUseCase().execute(taskId);
                return Result.mapError(result, () => undefined);
            },
            stopTask: async (taskId) => {
                const result = await this.getStopTaskUseCase().execute(taskId);
                return Result.mapError(result, () => undefined);
            },
            deleteTask: async (taskId) => {
                const result = await this.getDeleteTaskUseCase().execute(
                    taskId
                );
                return Result.mapError(result, () => undefined);
            },
            runAddTaskFlow: () => {
                this.runAddTaskFlow(backlogType, backlogDay, () => {
                    vm.reloadTasks(true);
                });
            },
            openTask: (taskId) => {
                this.runEditTaskFlow(taskId, () => vm.reloadTasks(true));
            },
        };

        vm.delegate = delegate;

        return vm;
    };

    public start = () => {
        const subscripbtion = this.listenAuthStateUseCase.listen(
            (authState) => {
                this.authState.set(authState);
            }
        );

        const startNewSessionUseCase = new StartNewSessionUseCaseImpl(
            this.authService,
            this.localTokensRepository,
            this.authStateRepository
        );

        startNewSessionUseCase.startNewSession();
    };
}
