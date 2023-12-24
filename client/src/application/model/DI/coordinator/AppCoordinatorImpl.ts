import AuthState from "../../../../domain/models/auth/AuthState";
import BacklogType from "../../../../domain/models/BacklogType";
import { TaskId } from "../../../../domain/models/Task";
import DayPageViewViewModel from "../../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import LogInPageViewModel from "../../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import Subject from "../../../../presentation/utils/Subject";
import value from "../../../../presentation/utils/value";
import AppModel, {
    AppModelPageFactories,
    CurrentModalState,
} from "./AppCoordinator";
import PresentationDIContainer from "../presentation/PresentationDIContainer";

export default class AppCoordinatorImpl implements AppModel {
    // Properties

    public currentModal: Subject<CurrentModalState | null> = value(null);
    public authState: Subject<AuthState> = value(AuthState.PROCESSING);

    // Constructor

    public constructor(private presentation: PresentationDIContainer) {}

    // Methods

    private runAddTaskFlow = (
        backlogType: BacklogType,
        backlogDay: string,
        didAdd: () => void
    ) => {
        const vm = this.presentation.makeAddTaskModalViewModel(
            backlogType,
            backlogDay,
            {
                didComplete: () => {
                    this.currentModal.set(null);
                    didAdd();
                },
                didHide: () => {
                    this.currentModal.set(null);
                },
            }
        );

        this.currentModal.set({
            type: "addTask",
            viewModel: vm,
        });
    };

    public makeBacklogDayPageViewModel = (
        backlogDay: string
    ): DayPageViewViewModel => {
        return this.presentation.makeBacklogDayPageViewModel(backlogDay, {
            runAddTaskFlow: this.runAddTaskFlow,
            runEditTaskFlow: this.runEditTaskFlow,
        });
    };

    public getPages = (): AppModelPageFactories => {
        return {
            makeLogInPageViewModel: this.makeLogInPageViewModel,
            makeBacklogDayPageViewModel: this.makeBacklogDayPageViewModel,
        };
    };

    public makeLogInPageViewModel = (): LogInPageViewModel => {
        return this.presentation.makeLoginPageModel();
    };

    public runEditTaskFlow = (
        taskId: TaskId,
        didCompleteUpdate: () => void
    ) => {
        const vm = this.presentation.makeEditTaskModalModel(taskId, {
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

    public start = () => {
        this.presentation.startListeningAuthStateChanges((newAuthState) => {
            this.authState.set(newAuthState);
        });
        this.presentation.startNewSession();
    };
}
