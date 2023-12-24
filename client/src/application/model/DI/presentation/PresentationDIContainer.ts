import AuthState from "../../../../domain/models/auth/AuthState";
import BacklogType from "../../../../domain/models/BacklogType";
import { TaskId } from "../../../../domain/models/Task";
import AddTaskViewModel from "../../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModel";
import EditTaskModalViewModel from "../../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import DayPageViewViewModel from "../../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import LogInPageViewModel from "../../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";

export default interface PresentationDIContainer {
    startNewSession(): void;

    startListeningAuthStateChanges(
        listener: (authState: AuthState) => void
    ): void;

    makeLoginPageModel(): LogInPageViewModel;

    makeEditTaskModalModel(
        taskId: TaskId,
        delegate: {
            hideModal: () => void;
            hideModalAfterCreation: () => void;
        }
    ): EditTaskModalViewModel;

    makeBacklogDayPageViewModel(
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
    ): DayPageViewViewModel;

    makeAddTaskModalViewModel(
        backlogType: BacklogType,
        backlogDay: string,
        delegate: {
            didComplete: () => void;
            didHide: () => void;
        }
    ): AddTaskViewModel;
}
