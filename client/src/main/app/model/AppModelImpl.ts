import LogInPageViewModel from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import LogInByUserNameAndPasswordUseCaseImpl from "../../../domain/usecases/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCaseImpl";
import DefaultAuthenticationService from "../../../data/services/DefaultAuthenticationService";
import { Configuration, DefaultApi } from "../../../data/API";
import ListenAuthenticationStateUseCaseImpl from "../../../domain/usecases/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCaseImpl";
import AuthTokensRepositoryImpl from "../../../data/repositories/AuthTokensRepositoryImpl";
import StartNewSessionUseCaseImpl, {
    LocalTokensRepository,
} from "../../../domain/usecases/StartNewSessionUseCase/StartNewSessionUseCaseImpl";
import AuthStateRepositoryImpl from "../../../data/repositories/AuthStateRepositoryImpl";
import AuthState from "../../../domain/models/AuthState";
import DayPageViewViewModel, {
    DayPageViewViewModelDelegate as DayPageViewModelDelegate,
} from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import ListTasksInBacklogUseCaseImpl from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import TasksRepositoryImpl from "../../../data/repositories/TasksRepositoryImpl";
import TaskMapperImpl from "../../../data/repositories/TaskMapper/TaskMapperImpl";
import StartTaskUseCaseImpl from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCaseImpl from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import DeleteTaskUseCaseImpl from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import BacklogType from "../../../domain/models/BacklogType";
import AddNewTaskUseCaseImpl from "../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import LogInPageViewModelImpl from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModelImpl";
import DayPageViewModelImpl from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModelImpl";
import AddTaskViewModelImpl from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModelImpl";
import Value from "../../../presentation/pages/LogInPage/Value";
import { value } from "../../../presentation/pages/LogInPage/DefaultValue";
import {
    IAuthenticationServiceByUserNameAndPassword,
    IAuthenticationServiceCheckToken,
} from "../../../domain/interfaces/services/AuthenticationService";
import StartTaskUseCase from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StopTaskUseCase from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import DeleteTaskUseCase from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import { IListenAuthenticationStateUseCaseOutput } from "../../../domain/usecases/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCase";
import AppModel, { AppModelPageFactories, CurrentModalState } from "./AppModel";
import AppSettings from "../AppSettings";
import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../../domain/models/Task";
import ListTasksInBacklogUseCase from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import EditTaskModalViewModelImpl from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModelImpl";
import { ILoadTaskUseCase } from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCase";
import LoadTaskUseCaseImpl from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCaseImpl";

export default class AppModelImpl implements AppModel {
    // Properties

    public currentModal: Value<CurrentModalState | null> = value(null);
    public authState: Value<AuthState> = value(AuthState.PROCESSING);

    private localTokensRepository: LocalTokensRepository;
    private apiClient: DefaultApi;
    private authService: IAuthenticationServiceByUserNameAndPassword &
        IAuthenticationServiceCheckToken;
    private authStateRepository: AuthStateRepositoryImpl;
    private listenAuthStateUseCase: IListenAuthenticationStateUseCaseOutput;

    // Constructor

    public constructor(settings: AppSettings) {
        this.localTokensRepository = new AuthTokensRepositoryImpl();

        this.apiClient = new DefaultApi(
            new Configuration({
                basePath: settings.api.basePath,
                accessToken: async () => {
                    const tokens = await this.localTokensRepository.getTokens();
                    return tokens?.accessToken ?? "";
                },
            })
        );

        this.authService = new DefaultAuthenticationService(this.apiClient);

        this.authStateRepository = new AuthStateRepositoryImpl();

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

    private getLoadTaskUseCase = (): ILoadTaskUseCase => {
        return new LoadTaskUseCaseImpl(this.getTasksRepository());
    };

    private makeEditTaskPageViewModel = (
        taskId: TaskId
    ): EditTaskModalViewModel => {
        return new EditTaskModalViewModelImpl(
            taskId,
            async () => {
                const useCase = this.getLoadTaskUseCase();

                const result = await useCase.execute(taskId);

                switch (result.type) {
                    case ResultType.Success:
                        return Result.success(result.value);
                    case ResultType.Failure:
                        return Result.failure(undefined);
                }
            },
            {
                updateTask: async (taskId, task) => {
                    return null as any;
                },
                didComplete: () => {
                    this.currentModal.set(null);
                },
                didHide: () => {
                    this.currentModal.set(null);
                },
            }
        );
    };

    public runEditTaskFlow = (taskId: TaskId) => {
        const vm = this.makeEditTaskPageViewModel(taskId);

        this.currentModal.set({
            type: "editTask",
            viewModel: vm,
        });
    };

    private makeBacklogDayPageViewModel = (
        backlogDay: string
    ): DayPageViewViewModel => {
        const backlogType = BacklogType.Day;

        const viewModel = new DayPageViewModelImpl();

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
                    viewModel.reloadTasks(true);
                });
            },
            openTask: (taskId) => {
                this.runEditTaskFlow(taskId);
            },
        };

        viewModel.delegate = delegate;

        return viewModel;
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
