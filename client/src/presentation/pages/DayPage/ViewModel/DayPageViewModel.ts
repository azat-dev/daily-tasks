import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import TaskStatus from "../../../../domain/models/TaskStatus";
import Value from "../../LogInPage/Value";
import { ActionButtonViewProps } from "../ActionButton/ActionButtonView";

export interface DayPageViewViewModelDelegate {
    loadTasks(): Promise<Result<Task[], undefined>>;
    loadStatuses(
        tasksIds: TaskId[]
    ): Promise<Result<Record<TaskId, TaskStatus>, undefined>>;
    runAddTaskFlow(): void;
    startTask(taskId: TaskId): Promise<Result<Date, undefined>>;
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

export interface DayPageViewViewModelOutput {
    isLoading: Value<boolean>;
    rows: Value<DayPageViewViewModelRow[]>;
}

export interface DayPageViewViewModelInput {
    load: () => Promise<void>;
    onAddTask: () => void;
}

export interface DayPageViewViewModelUpdateProperties {
    reloadTasks(silent: boolean): void;
}

interface DayPageViewViewModel
    extends DayPageViewViewModelInput,
        DayPageViewViewModelOutput,
        DayPageViewViewModelUpdateProperties {
    delegate: DayPageViewViewModelDelegate | null;
}

export default DayPageViewViewModel;
