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
import Subject from "../../../presentation/utils/Subject";
import value from "../../../presentation/utils/value";
import {
    AuthenticationServiceByUserNameAndPassword,
    AuthenticationServiceCheckToken,
} from "../../../domain/interfaces/services/AuthenticationService";
import StartTaskUseCase from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StopTaskUseCase from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import DeleteTaskUseCase from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import { ListenAuthenticationStateUseCaseOutput } from "../../../domain/usecases/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCase";
import AppModel, { AppModelPageFactories, CurrentModalState } from "./AppModel";
import AppSettings from "../AppSettings";
import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../../domain/models/Task";
import ListTasksInBacklogUseCase from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import EditTaskModalViewModelImpl from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModelImpl";
import { LoadTaskUseCase } from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCase";
import LoadTaskUseCaseImpl from "../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCaseImpl";
import EditTaskUseCaseImpl from "../../../domain/usecases/EditTaskUseCase/AddNewTaskUseCaseImpl";
import EditTaskUseCase from "../../../domain/usecases/EditTaskUseCase/EditTaskUseCase";

export default class AppModelImpl implements AppModel {
    // Properties

    public currentModal: Subject<CurrentModalState | null> = value(null);
    public authState: Subject<AuthState> = value(AuthState.PROCESSING);

    private localTokensRepository: LocalTokensRepository;
    private apiClient: DefaultApi;
    private authService: AuthenticationServiceByUserNameAndPassword &
        AuthenticationServiceCheckToken;
    private authStateRepository: AuthStateRepositoryImpl;
    private listenAuthStateUseCase: ListenAuthenticationStateUseCaseOutput;

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
        didCompleteUpdate: () => void
    ): EditTaskModalViewModel => {
        const vm = new EditTaskModalViewModelImpl(taskId, async () => {
            const useCase = this.getLoadTaskUseCase();
            const result = await useCase.execute(taskId);
            return Result.mapError(result, () => undefined);
        });

        vm.delegate = {
            updateTask: async (taskId, task) => {
                const useCase = this.getEditTaskUseCase();
                const result = await useCase.execute(taskId, task);

                return Result.mapError(result, () => undefined);
            },
            didComplete: () => {
                didCompleteUpdate();
                this.currentModal.set(null);
            },
            didHide: () => {
                this.currentModal.set(null);
            },
        };

        return vm;
    };

    public runEditTaskFlow = (
        taskId: TaskId,
        didCompleteUpdate: () => void
    ) => {
        const vm = this.makeEditTaskPageViewModel(taskId, didCompleteUpdate);

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
                this.runEditTaskFlow(taskId, () => viewModel.reloadTasks(true));
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
