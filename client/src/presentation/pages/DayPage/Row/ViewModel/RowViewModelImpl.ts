import Task from "../../../../../domain/models/Task";
import TaskPriority from "../../../../../domain/models/TaskPriority";
import TaskStatus from "../../../../../domain/models/TaskStatus";
import { value } from "../../../LogInPage/DefaultValue";
import Value from "../../../LogInPage/Value";
import { ActionButtonViewProps } from "../../ActionButton/ActionButtonView";
import RowViewModel, { RowViewModelDelegate } from "./RowViewModel";

const mapPriority = (priority: TaskPriority | undefined) => {
    switch (priority) {
        case TaskPriority.LOW:
            return "Low";
        case TaskPriority.MEDIUM:
            return "Medium";
        case TaskPriority.HIGH:
            return "High";
        default:
            return "---";
    }
};

const mapStatus = (status: TaskStatus) => {
    switch (status) {
        case TaskStatus.Archived:
            return "Archived";
        case TaskStatus.Completed:
            return "Completed";
        case TaskStatus.InProgress:
            return "In Progress";
        case TaskStatus.NotStarted:
            return "Not Started";
        default:
            return "---";
    }
};

const formatCreatedAt = (createdAt: Date) => {
    return createdAt.toLocaleDateString("ru-RU", {
        year: "numeric",
        month: "numeric",
        day: "numeric",
    });
};

export default class RowViewModelImpl implements RowViewModel {
    // Methods
    public key: string;
    public title: Value<string>;
    public createdAt: Value<string>;
    public status: Value<string>;
    public priority: Value<string>;
    public isActive: Value<boolean>;
    public actionButtonViewModel: ActionButtonViewProps;

    public delegate: RowViewModelDelegate | null = null;

    constructor(task: Task, delegate: RowViewModelDelegate | null = null) {
        this.delegate = delegate;
        const taskId = task.id;

        this.key = `${task.id}`;
        this.title = value(task.title);
        this.isActive = value(task.status === TaskStatus.InProgress);
        this.createdAt = value(formatCreatedAt(task.createdAt));
        this.status = value(mapStatus(task.status));
        this.priority = value(mapPriority(task.priority));

        this.actionButtonViewModel = {
            startedAt: null,
            onStart: () => {
                this.delegate!.onStart(taskId);
            },
            onStop: () => {
                this.delegate!.onStop(taskId);
            },
            onDelete: () => {
                this.delegate!.onDelete(taskId);
            },
            onDoLaterWeek: () => {
                this.delegate!.onDoLaterWeek(taskId);
            },
            onDoLaterMonth: () => {
                this.delegate!.onDoLaterMonth(taskId);
            },
        };
    }

    updateStatus = (status: TaskStatus): void => {
        this.status.set(mapStatus(status));
        this.isActive.set(status === TaskStatus.InProgress);
    };
}
