import LogInPageViewModel from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import AddTaskViewModel from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModel";
import DayPageViewViewModel from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import AuthState from "../../../domain/models/AuthState";
import Subject from "../../../presentation/utils/Subject";
import EditTaskModalViewModel from "../../../presentation/modals/EditTaskModal/ViewModel/EditTaskModalViewModel";

export type CurrentModalState =
    | {
          type: "addTask";
          viewModel: AddTaskViewModel;
      }
    | {
          type: "editTask";
          viewModel: EditTaskModalViewModel;
      };

export interface AppModelPageFactories {
    makeLogInPageViewModel(): LogInPageViewModel;
    makeBacklogDayPageViewModel(backlogDay: string): DayPageViewViewModel;
}

export interface AppModelOutput {
    currentModal: Subject<CurrentModalState | null>;
    authState: Subject<AuthState>;
    getPages(): AppModelPageFactories;
}

export interface AppModelInput {
    start(): void;
}

export default interface AppModel extends AppModelInput, AppModelOutput {}
