import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import UpdateTaskData from "../../../../domain/models/UpdateTaskData";
import ISubject from "../../../utils/ISubject";

export interface EditTaskModalViewModelDelegate {
    updateTask: (
        taskId: TaskId,
        data: UpdateTaskData
    ) => Promise<Result<Task, undefined>>;
    didComplete: () => void;
    didHide: () => void;
}

export interface EditTaskModalViewModelOutput {
    isProcessing: ISubject<boolean>;
    isLoading: ISubject<boolean>;
    show: ISubject<boolean>;
    title: ISubject<string>;
    highlightTitleAsError: ISubject<boolean>;
    description: ISubject<string>;
    priority: ISubject<string | undefined>;
    priorityOptions: { value: string; label: string }[];
}

export interface EditTaskModalViewModelInput {
    onChangeTitle: (e: any) => void;
    onChangeDescription: (e: any) => void;
    onCancel: (e: any) => void;
    onHide: () => void;
    onSave: (e: any) => void;
    onChangePriority: (e: any) => void;
    onUnMount: () => void;

    load: () => void;
}

export default interface EditTaskModalViewModel
    extends EditTaskModalViewModelInput,
        EditTaskModalViewModelOutput {}
