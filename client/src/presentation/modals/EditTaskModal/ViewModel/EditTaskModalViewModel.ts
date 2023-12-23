import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import UpdateTaskData from "../../../../domain/models/UpdateTaskData";
import Subject from "../../../utils/Subject";

export interface EditTaskModalViewModelDelegate {
    updateTask: (
        taskId: TaskId,
        data: UpdateTaskData
    ) => Promise<Result<Task, undefined>>;
    didComplete: () => void;
    didHide: () => void;
}

export interface EditTaskModalViewModelOutput {
    isProcessing: Subject<boolean>;
    isLoading: Subject<boolean>;
    show: Subject<boolean>;
    title: Subject<string>;
    highlightTitleAsError: Subject<boolean>;
    description: Subject<string>;
    priority: Subject<string | undefined>;
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
