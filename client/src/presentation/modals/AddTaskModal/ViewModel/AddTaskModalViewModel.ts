import Value from "../../../pages/LogInPage/Value";

interface AddTaskViewModelOutput {
    isProcessing: Value<boolean>;
    show: Value<boolean>;
    title: Value<string>;
    highlightTitleAsError: Value<boolean>;
    description: Value<string>;
    priority: Value<string | undefined>;
    priorityOptions: { value: string; label: string }[];
}

interface AddTaskViewModelInput {
    onChangeTitle: (e: any) => void;
    onChangeDescription: (e: any) => void;
    onHide: () => void;
    onCancel: (e: any) => void;
    onSave: (e: any) => void;
    onChangePriority: (e: any) => void;
}

export default interface AddTaskViewModel
    extends AddTaskViewModelInput,
        AddTaskViewModelOutput {}
