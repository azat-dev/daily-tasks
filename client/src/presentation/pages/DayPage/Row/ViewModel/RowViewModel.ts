import { TaskId } from "../../../../../domain/models/Task";
import TaskStatus from "../../../../../domain/models/TaskStatus";
import Subject from "../../../../utils/Subject";
import ActionButtonViewModel from "../../ActionButton/ActionButtonViewModel";

export interface RowViewModelDelegate {
    onOpen: (taskId: TaskId) => void;
    onStart(taskId: TaskId): void;
    onStop(taskId: TaskId): void;
    onDelete(taskId: TaskId): void;
    onDoLaterWeek(taskId: TaskId): void;
    onDoLaterMonth(taskId: TaskId): void;
}

export interface RowViewModelOutput {
    key: string;
    title: Subject<string>;
    createdAt: Subject<string>;
    status: Subject<string>;
    priority: Subject<string>;
    isActive: Subject<boolean>;
    actionButtonViewModel: ActionButtonViewModel;
}

export interface RowViewModelInput {
    onClick: (e: any) => void;
}

export interface RowViewModelUpdateProperties {
    updateStatus(status: TaskStatus): void;
}

export default interface RowViewModel
    extends RowViewModelOutput,
        RowViewModelInput,
        RowViewModelUpdateProperties {
    delegate: RowViewModelDelegate | null;
}
