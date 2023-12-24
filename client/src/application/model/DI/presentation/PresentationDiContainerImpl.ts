import { Result, ResultType } from "../../../../common/Result";
import AuthState from "../../../../domain/models/auth/AuthState";
import BacklogType from "../../../../domain/models/BacklogType";
import { TaskId } from "../../../../domain/models/Task";
import AddTaskViewModelImpl from "../../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModelImpl";
import EditTaskModalViewModelImpl from "../../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModelImpl";
import DayPageViewViewModel, {
    DayPageViewViewModelDelegate,
} from "../../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import DayPageViewModelImpl from "../../../../presentation/pages/DayPage/ViewModel/DayPageViewModelImpl";
import LogInPageViewModelImpl from "../../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModelImpl";
import PresentationDIContainer from "./PresentationDIContainer";
import DomainDIContainer from "../domain/DomainDIContainer";

export default class PresentationDiContainerImpl
    implements PresentationDIContainer
{
    constructor(private domain: DomainDIContainer) {}

    startNewSession = () => {
        this.domain.getStartNewSessionUseCase().startNewSession();
    };

    startListeningAuthStateChanges = (
        listener: (authState: AuthState) => void
    ) => {
        const subscripbtion = this.domain
            .getListenAuthenticationStateUseCase()
            .listen(listener);
    };

    makeLoginPageModel = () => {
        return new LogInPageViewModelImpl(async (username, password) => {
            const result = await this.domain
                .getLogInByUserNameAndPasswordUseCase()
                .execute(username, password);

            return result.type === ResultType.Success;
        });
    };

    makeEditTaskModalModel = (
        taskId: TaskId,
        delegate: {
            hideModal: () => void;
            hideModalAfterCreation: () => void;
        }
    ) => {
        const vm = new EditTaskModalViewModelImpl(taskId, async () => {
            const result = await this.domain
                .getLoadTaskUseCase()
                .execute(taskId);
            return Result.eraseError(result);
        });

        vm.delegate = {
            updateTask: async (taskId, task) => {
                const result = await this.domain
                    .getEditTaskUseCase()
                    .execute(taskId, task);

                return Result.eraseError(result);
            },
            didComplete: delegate.hideModalAfterCreation,
            didHide: delegate.hideModal,
        };

        return vm;
    };

    makeBacklogDayPageViewModel = (
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
                const result = await this.domain
                    .getListTasksInBacklogUseCase(backlogType)
                    .execute(backlogDay);
                return Result.eraseError(result);
            },
            loadStatuses: async () => {
                return Result.success({});
            },
            startTask: async (taskId) => {
                const result = await this.domain
                    .getStartTaskUseCase()
                    .execute(taskId);
                return Result.eraseError(result);
            },
            stopTask: async (taskId) => {
                const result = await this.domain
                    .getStopTaskUseCase()
                    .execute(taskId);
                return Result.eraseError(result);
            },
            deleteTask: async (taskId) => {
                const result = await this.domain
                    .getDeleteTaskUseCase()
                    .execute(taskId);
                return Result.eraseError(result);
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

    makeAddTaskModalViewModel = (
        backlogType: BacklogType,
        backlogDay: string,
        delegate: {
            didComplete: () => void;
            didHide: () => void;
        }
    ) => {
        return new AddTaskViewModelImpl({
            createTask: async (newTask): Promise<Result<TaskId, undefined>> => {
                const result = await this.domain
                    .getAddNewTaskUseCase()
                    .execute(backlogType, backlogDay, newTask);

                if (result.type === ResultType.Success) {
                    return Result.success(result.value);
                }

                return Result.failure(undefined);
            },
            didComplete: delegate.didComplete,
            didHide: delegate.didHide,
        });
    };
}
