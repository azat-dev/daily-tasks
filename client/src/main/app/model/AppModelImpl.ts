import { Result, ResultType } from "../../../common/Result";
import { DefaultApi } from "../../../data/API";
import TaskMapperImpl from "../../../data/repositories/TaskMapper/TaskMapperImpl";
import TasksRepositoryImpl from "../../../data/repositories/TasksRepositoryImpl";
import AuthState from "../../../domain/models/auth/AuthState";
import BacklogType from "../../../domain/models/BacklogType";
import { TaskId } from "../../../domain/models/Task";
import AddNewTaskUseCaseImpl from "../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import DeleteTaskUseCase from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import DeleteTaskUseCaseImpl from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import ListTasksInBacklogUseCase from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import ListTasksInBacklogUseCaseImpl from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import StartTaskUseCase from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StartTaskUseCaseImpl from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCase from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import StopTaskUseCaseImpl from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import AddTaskViewModelImpl from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModelImpl";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import DayPageViewViewModel, {
    DayPageViewViewModelDelegate as DayPageViewModelDelegate,
} from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import DayPageViewModelImpl from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModelImpl";
import LogInPageViewModel from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import Subject from "../../../presentation/utils/Subject";
import value from "../../../presentation/utils/value";
import AppModel, { AppModelPageFactories, CurrentModalState } from "./AppModel";

export default class AppModelImpl implements AppModel {
    // Properties

    public currentModal: Subject<CurrentModalState | null> = value(null);
    public authState: Subject<AuthState> = value(AuthState.PROCESSING);

    // Constructor

    public constructor(
        private readonly apiClient: DefaultApi,
        private readonly startNewSession: () => void,
        private readonly startListeningAuthStateChanges: (
            listener: (authState: AuthState) => void
        ) => void,
        private readonly makeLogInPageModel: () => LogInPageViewModel,
        private readonly makeEditTaskPageViewModel: (
            taskId: TaskId,
            delegate: {
                hideModal: () => void;
                hideModalAfterCreation: () => void;
            }
        ) => EditTaskModalViewModel
    ) {}

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

    public makeLogInPageViewModel = (): LogInPageViewModel => {
        return this.makeLogInPageModel();
    };

    public runEditTaskFlow = (
        taskId: TaskId,
        didCompleteUpdate: () => void
    ) => {
        const vm = this.makeEditTaskPageViewModel(taskId, {
            hideModal: () => {
                this.currentModal.set(null);
            },
            hideModalAfterCreation: () => {
                didCompleteUpdate();
                this.currentModal.set(null);
            },
        });

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
        this.startListeningAuthStateChanges((newAuthState) => {
            this.authState.set(newAuthState);
        });
        this.startNewSession();
    };
}
