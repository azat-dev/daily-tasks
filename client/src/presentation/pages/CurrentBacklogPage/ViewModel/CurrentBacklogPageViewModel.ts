import Task from "../../../../domain/models/Task";
import Value from "../../LogInPage/Value";

export interface CurrentBacklogPageViewModelOutput {
    isLoading: Value<boolean>;
    tasks: Value<Task[]>;
}

export interface CurrentBacklogPageViewModelInput {
    onClickAddTask: (e: any) => void;
}

interface CurrentBacklogPageViewModel
    extends CurrentBacklogPageViewModelInput,
        CurrentBacklogPageViewModelOutput {}

export default CurrentBacklogPageViewModel;
