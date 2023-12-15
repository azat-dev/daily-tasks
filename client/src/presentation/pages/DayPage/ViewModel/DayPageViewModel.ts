import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import Value from "../../LogInPage/Value";
import { ActionButtonViewProps } from "../ActionButton/ActionButtonView";

export interface DayPageViewViewModelDelegate {
    loadTasks(): Promise<Result<Task[], undefined>>;
    startTask(taskId: TaskId): Promise<Result<undefined, undefined>>;
    stopTask(taskId: TaskId): Promise<Result<undefined, undefined>>;
    deleteTask(taskId: TaskId): Promise<Result<undefined, undefined>>;
}

export interface DayPageViewViewModelRow {
    key: string;
    title: string;
    createdAt: string;
    status: string;
    priority: string;
    isActive: boolean;
    actionButtonViewModel: ActionButtonViewProps;
}

interface DayPageViewViewModel {
    isLoading: Value<boolean>;
    rows: Value<DayPageViewViewModelRow[]>;
    load: () => Promise<void>;
}

export default DayPageViewViewModel;
