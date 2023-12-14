import LogInPageViewModel from "../../../presentation/pages/LogInPage/ViewModel/LogInPageViewModel";
import AddTaskViewModel from "../../../presentation/modals/AddTaskModal/ViewModel/AddTaskModalViewModel";
import CurrentBacklogPageViewModel from "../../../presentation/pages/CurrentBacklogPage/ViewModel/CurrentBacklogPageViewModel";
import DayPageViewViewModel from "../../../presentation/pages/DayPage/ViewModel/DayPageViewModel";
import AuthState from "../../../domain/models/AuthState";
import BacklogType from "../../../domain/models/BacklogType";
import Value from "../../../presentation/pages/LogInPage/Value";

export type CurrentModalState = {
    type: "addTask";
    viewModel: AddTaskViewModel;
};

export interface AppModelPageFactories {
    makeLogInPageViewModel(): LogInPageViewModel;

    makeBacklogPageViewModel(
        backlogType: BacklogType,
        backlogDay: string
    ): CurrentBacklogPageViewModel;

    makeBacklogDayPageViewModel(backlogDay: string): DayPageViewViewModel;
}

export interface AppModelOutput {
    currentModal: Value<CurrentModalState | null>;
    authState: Value<AuthState>;
    getPages(): AppModelPageFactories;
}

export interface AppModelInput {
    start(): void;
}

export default interface AppModel extends AppModelInput, AppModelOutput {}
