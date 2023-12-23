import { TaskId } from "../../../../../domain/models/Task";
import TaskStatus from "../../../../../domain/models/TaskStatus";
import ISubject from "../../../../utils/ISubject";
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
    title: ISubject<string>;
    createdAt: ISubject<string>;
    status: ISubject<string>;
    priority: ISubject<string>;
    isActive: ISubject<boolean>;
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
