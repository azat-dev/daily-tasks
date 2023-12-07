import { useState } from "react";
import AddTaskViewModel from "./AddTaskModalViewModel";
import AddNewTaskUseCase from "../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCase";
import BacklogType from "../../../domain/models/BacklogType";

const priorityOptions = [
    { value: 1, label: "Low" },
    { value: 2, label: "Medium" },
    { value: 3, label: "High" },
];

const useAddTaskViewModel = (
    backlogType: BacklogType,
    backlogDay: string,
    addNewTask: AddNewTaskUseCase
): AddTaskViewModel => {
    const [isProcessing, setIsProcessing] = useState<boolean>(false);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [priority, setPriority] = useState<number | undefined>(undefined);
    const [highlightTitleAsError, setHighlightTitleAsError] = useState(false);

    const onChangeTitle = (e: any) => {
        setTitle(e.target.value);
        setHighlightTitleAsError(false);
    };

    const onChangeDescription = (e: any) => {
        setDescription(e.target.value);
    };

    const onHide = () => {
        setTitle("");
        setDescription("");
    };

    const onCancel = (e: any) => {
        e.preventDefault();
        e.stopPropagation();
        onHide();
    };

    const onSave = (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        setIsProcessing(true);

        const cleanedTitle = title.trim();
        if (!cleanedTitle) {
            setHighlightTitleAsError(true);
            setIsProcessing(false);
            return;
        }

        addNewTask.execute(backlogType, backlogDay, {
            title: cleanedTitle,
            description: description,
            priority: priority,
        });
    };

    const onChangePriority = (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        setPriority(e.target.value);
    };

    return {
        show: true,
        isProcessing,
        title,
        onChangeTitle,
        highlightTitleAsError,
        description,
        onChangeDescription,
        onCancel,
        onSave,
        onHide,
        priority: priority,
        priorityOptions,
        onChangePriority,
    };
};

export default useAddTaskViewModel;
