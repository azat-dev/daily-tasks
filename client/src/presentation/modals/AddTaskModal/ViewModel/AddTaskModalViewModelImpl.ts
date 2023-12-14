import AddTaskViewModel from "./AddTaskModalViewModel";
import AddNewTaskUseCase from "../../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCase";
import BacklogType from "../../../../domain/models/BacklogType";
import TaskPriority from "../../../../domain/models/TaskPriority";
import Value from "../../../pages/LogInPage/Value";
import { value } from "../../../pages/LogInPage/DefaultValue";

export default class AddTaskViewModelImpl implements AddTaskViewModel {
    // Properties

    public isProcessing = value(false);
    public show = value(true);
    public title = value("");
    public highlightTitleAsError: Value<boolean> = value(false);
    public description = value("");
    public priority: Value<string | undefined> = value(undefined);
    public priorityOptions = [
        { value: TaskPriority.LOW, label: "Low" },
        { value: TaskPriority.MEDIUM, label: "Medium" },
        { value: TaskPriority.HIGH, label: "High" },
    ];

    public constructor(
        private backlogType: BacklogType,
        private backlogDay: string,
        private addNewTask: AddNewTaskUseCase
    ) {}

    // Methods

    public onChangeTitle = (e: any) => {
        this.title.set(e.target.value);
        this.highlightTitleAsError.set(false);
    };

    onChangeDescription = (e: any) => {
        this.description.set(e.target.value);
    };

    onHide = () => {
        this.title.set("");
        this.description.set("");
    };

    onCancel = (e: any) => {
        e.preventDefault();
        e.stopPropagation();
        this.onHide();
    };

    onSave = async (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        this.isProcessing.set(true);

        const cleanedTitle = this.title.value.trim();
        if (!cleanedTitle) {
            this.highlightTitleAsError.set(true);
            this.isProcessing.set(false);
            return;
        }

        await this.addNewTask.execute(this.backlogType, this.backlogDay, {
            title: cleanedTitle,
            description: this.description.value,
            priority: this.priority.value as any,
        });
    };

    onChangePriority = (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        this.priority.set(e.target.value);
    };
}
