import { TaskId } from "../../../../../domain/models/Task";
import TaskStatus from "../../../../../domain/models/TaskStatus";
import Value from "../../../LogInPage/Value";
import { ActionButtonViewProps } from "../../ActionButton/ActionButtonView";

export interface RowViewModelDelegate {
    onStart(taskId: TaskId): void;
    onStop(taskId: TaskId): void;
    onDelete(taskId: TaskId): void;
    onDoLaterWeek(taskId: TaskId): void;
    onDoLaterMonth(taskId: TaskId): void;
}

export interface RowViewModelOutput {
    key: string;
    title: Value<string>;
    createdAt: Value<string>;
    status: Value<string>;
    priority: Value<string>;
    isActive: Value<boolean>;
    actionButtonViewModel: ActionButtonViewProps;
}

export interface RowViewModelUpdateProperties {
    updateStatus(status: TaskStatus): void;
}

export default interface RowViewModel
    extends RowViewModelOutput,
        RowViewModelUpdateProperties {
    delegate: RowViewModelDelegate | null;
}
