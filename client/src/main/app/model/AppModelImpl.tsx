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
import CurrentBacklogPageViewModel from "../../../presentation/pages/CurrentBacklogPage/ViewModel/CurrentBacklogPageViewModel";
import DayPageViewViewModel from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import ListTasksInBacklogUseCaseImpl from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import TasksRepositoryImpl from "../../../data/repositories/TasksRepositoryImpl";
import TaskMapperImpl from "../../../data/repositories/TaskMapper/TaskMapperImpl";
import StartTaskUseCaseImpl from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCaseImpl from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import DeleteTaskUseCaseImpl from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import BacklogType from "../../../domain/models/BacklogType";
import AddNewTaskUseCaseImpl from "../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import LogInPageViewModelImpl from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModelImpl";
import CurrentBacklogPageViewModelImpl from "../../../presentation/pages/CurrentBacklogPage/ViewModel/CurrentBacklogPageViewModelImpl";
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

    private runAddTaskFlow = (backlogType: BacklogType, backlogDay: string) => {
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
                        return {
                            type: ResultType.Success,
                            value: result.value,
                        };
                    }
                    return {
                        type: ResultType.Failure,
                        error: undefined,
                    };
                },
                didComplete: () => {
                    this.currentModal.set(null);
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
            makeBacklogPageViewModel: this.makeBacklogPageViewModel,
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

    private makeBacklogPageViewModel = (
        backlogType: BacklogType,
        backlogDay: string
    ): CurrentBacklogPageViewModel => {
        return new CurrentBacklogPageViewModelImpl(() => {
            this.runAddTaskFlow(backlogType, backlogDay);
        });
    };

    private makeBacklogDayPageViewModel = (
        backlogDay: string
    ): DayPageViewViewModel => {
        const listCurrentDayTasksUseCase = new ListTasksInBacklogUseCaseImpl(
            BacklogType.Day,
            this.getTasksRepository()
        );

        return new DayPageViewModelImpl(
            backlogDay,
            listCurrentDayTasksUseCase,
            this.getStartTaskUseCase(),
            this.getStopTaskUseCase(),
            this.getDeleteTaskUseCase()
        );
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
