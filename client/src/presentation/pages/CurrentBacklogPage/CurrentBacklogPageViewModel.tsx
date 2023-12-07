import Task from "../../../domain/models/Task";

interface CurrentBacklogPageViewModel {
    isLoading: boolean;
    onClickAddTask: (e: any) => void;
    tasks: Task[];
}

export default CurrentBacklogPageViewModel;
