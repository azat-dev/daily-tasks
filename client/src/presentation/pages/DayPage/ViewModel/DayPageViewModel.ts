import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import TaskStatus from "../../../../domain/models/TaskStatus";
import Value from "../../LogInPage/Value";
import RowViewModel from "../Row/ViewModel/RowViewModel";

export interface DayPageViewViewModelDelegate {
    loadTasks(): Promise<Result<Task[], undefined>>;
    loadStatuses(
        tasksIds: TaskId[]
    ): Promise<Result<Record<TaskId, TaskStatus>, undefined>>;
    runAddTaskFlow(): void;
    openTask(taskId: TaskId): void;
    startTask(taskId: TaskId): Promise<Result<Date, undefined>>;
    stopTask(taskId: TaskId): Promise<Result<Date, undefined>>;
    deleteTask(taskId: TaskId): Promise<Result<undefined, undefined>>;
}

export interface DayPageViewViewModelRowUpdateProperties {}

export interface DayPageViewViewModelOutput {
    isLoading: Value<boolean>;
    rows: Value<RowViewModel[]>;
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
