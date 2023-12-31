import { Result } from "../../../../common/Result";
import NewTaskData from "../../../../domain/models/NewTaskData";
import { TaskId } from "../../../../domain/models/Task";
import Value from "../../../pages/LogInPage/Value";

export interface AddTaskViewModelDelegate {
    createTask: (data: NewTaskData) => Promise<Result<TaskId, undefined>>;
    didComplete: () => void;
    didHide: () => void;
}

export interface AddTaskViewModelOutput {
    isProcessing: Value<boolean>;
    show: Value<boolean>;
    title: Value<string>;
    highlightTitleAsError: Value<boolean>;
    description: Value<string>;
    priority: Value<string | undefined>;
    priorityOptions: { value: string; label: string }[];
}

export interface AddTaskViewModelInput {
    onChangeTitle: (e: any) => void;
    onChangeDescription: (e: any) => void;
    onCancel: (e: any) => void;
    onHide: () => void;
    onSave: (e: any) => void;
    onChangePriority: (e: any) => void;
    onUnMount: () => void;
}

export default interface AddTaskViewModel
    extends AddTaskViewModelInput,
        AddTaskViewModelOutput {}
