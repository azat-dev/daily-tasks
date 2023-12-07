interface AddTaskViewModel {
    isProcessing: boolean;
    show: boolean;
    title: string;
    highlightTitleAsError: boolean;
    onChangeTitle: (e: any) => void;
    description: string;
    onChangeDescription: (e: any) => void;
    onHide: () => void;
    onCancel: (e: any) => void;
    onSave: (e: any) => void;
    onChangePriority: (e: any) => void;
    priority: number | undefined;
    priorityOptions: { value: number; label: string }[];
}

export default AddTaskViewModel;
