import { Result } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";
import UpdateTaskData from "../../../../domain/models/UpdateTaskData";
import Value from "../../../pages/LogInPage/Value";

export interface EditTaskModalViewModelDelegate {
    updateTask: (
        taskId: TaskId,
        data: UpdateTaskData
    ) => Promise<Result<TaskId, undefined>>;
    didComplete: () => void;
    didHide: () => void;
}

export interface EditTaskModalViewModelOutput {
    isProcessing: Value<boolean>;
    show: Value<boolean>;
    title: Value<string>;
    highlightTitleAsError: Value<boolean>;
    description: Value<string>;
    priority: Value<string | undefined>;
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
