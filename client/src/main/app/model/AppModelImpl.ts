import AuthState from "../../../domain/models/auth/AuthState";
import BacklogType from "../../../domain/models/BacklogType";
import { TaskId } from "../../../domain/models/Task";

import AddTaskViewModel from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModel";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";
import DayPageViewViewModel from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
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
        ) => EditTaskModalViewModel,
        private readonly makeBacklogDayPageModel: (
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
        ) => DayPageViewViewModel,
        private readonly makeAddTaskModalModel: (
            backlogType: BacklogType,
            backlogDay: string,
            delegate: {
                didComplete: () => void;
                didHide: () => void;
            }
        ) => AddTaskViewModel
    ) {}

    // Methods

    private runAddTaskFlow = (
        backlogType: BacklogType,
        backlogDay: string,
        didAdd: () => void
    ) => {
        const vm = this.makeAddTaskModalModel(backlogType, backlogDay, {
            didComplete: () => {
                this.currentModal.set(null);
                didAdd();
            },
            didHide: () => {
                this.currentModal.set(null);
            },
        });

        this.currentModal.set({
            type: "addTask",
            viewModel: vm,
        });
    };

    public makeBacklogDayPageViewModel = (
        backlogDay: string
    ): DayPageViewViewModel => {
        return this.makeBacklogDayPageModel(backlogDay, {
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

    public start = () => {
        this.startListeningAuthStateChanges((newAuthState) => {
            this.authState.set(newAuthState);
        });
        this.startNewSession();
    };
}
