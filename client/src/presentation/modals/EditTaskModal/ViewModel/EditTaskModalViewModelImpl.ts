import EditTaskModalViewModel, {
    EditTaskModalViewModelDelegate,
} from "./EditTaskModalViewModel";
import TaskPriority from "../../../../domain/models/TaskPriority";
import Value from "../../../pages/LogInPage/Value";
import { value } from "../../../pages/LogInPage/DefaultValue";
import { Result, ResultType } from "../../../../common/Result";
import Task, { TaskId } from "../../../../domain/models/Task";

export default class EditTaskViewModelImpl implements EditTaskModalViewModel {
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
        private taskId: TaskId,
        private loadTask: (taskId: TaskId) => Promise<Result<Task, undefined>>,
        private delegate: EditTaskModalViewModelDelegate
    ) {}

    // Methods

    public onChangeTitle = (e: any) => {
        this.title.set(e.target.value);
        this.highlightTitleAsError.set(false);
    };

    onChangeDescription = (e: any) => {
        this.description.set(e.target.value);
    };

    onUnMount = () => {
        this.delegate.didHide();
    };

    onHide = () => {
        this.title.set("");
        this.description.set("");
        this.show.set(false);
    };

    onCancel = (e: any) => {
        e.preventDefault();
        e.stopPropagation();
        this.show.set(false);
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

        const result = await this.delegate.updateTask(this.taskId, {
            title: cleanedTitle,
            description: this.description.value,
            priority: this.priority.value as any,
        });

        this.isProcessing.set(false);

        if (result.type === ResultType.Failure) {
            return;
        }

        this.show.set(false);
        this.delegate.didComplete();
    };

    onChangePriority = (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        this.priority.set(e.target.value);
    };

    load = async (): Promise<void> => {
        debugger;
        const result = await this.loadTask(this.taskId);

        if (result.type === ResultType.Failure) {
            return;
        }

        const task = result.value;

        this.title.set(task.title);
        this.description.set(task.description ?? "");
        this.priority.set(task.priority);
    };
}
