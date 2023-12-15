import { ResultType } from "../../../../common/Result";
import DayPageViewModel, {
    DayPageViewViewModelDelegate,
    DayPageViewViewModelRow,
} from "./DayPageViewModel";
import TaskStatus from "../../../../domain/models/TaskStatus";
import Task, { TaskId } from "../../../../domain/models/Task";
import TaskPriority from "../../../../domain/models/TaskPriority";
import Value from "../../LogInPage/Value";
import { value } from "../../LogInPage/DefaultValue";

const mapPriority = (priority: TaskPriority | undefined) => {
    switch (priority) {
        case undefined:
            return "";
        case TaskPriority.LOW:
            return "Low";
        case TaskPriority.MEDIUM:
            return "Medium";
        case TaskPriority.HIGH:
            return "High";
        default:
            return "";
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
    }
};

const formatCreatedAt = (createdAt: Date) => {
    return createdAt.toLocaleDateString("ru-RU", {
        year: "numeric",
        month: "numeric",
        day: "numeric",
    });
};

const mapTaskToRow = (
    task: Task,
    onStart: () => void,
    onStop: () => void,
    onDelete: () => void
): DayPageViewViewModelRow => {
    return {
        key: String(task.id),
        title: task.title,
        createdAt: formatCreatedAt(task.createdAt),
        status: mapStatus(task.status),
        priority: mapPriority(task.priority),
        isActive: task.status === TaskStatus.InProgress,
        actionButtonViewModel: {
            startedAt: null,
            onStart,
            onStop,
            onDelete,
            onDoLaterWeek: () => {},
            onDoLaterMonth: () => {},
        },
    };
};

export default class DayPageViewModelImpl implements DayPageViewModel {
    // Properties
    public isLoading: Value<boolean> = value(true);
    public rows: Value<DayPageViewViewModelRow[]> = value([]);

    // Constructor

    constructor(public delegate: DayPageViewViewModelDelegate | null = null) {}

    // Methods

    private loadTasksSilently = async () => {
        const result = await this.delegate!.loadTasks();

        if (result.type !== ResultType.Success) {
            return;
        }

        const startTask = async (taskId: TaskId) => {
            await this.delegate!.startTask(taskId);
        };

        const stopTask = (taskId: TaskId) => {
            this.delegate!.stopTask(taskId);
        };

        const deleteTask = (taskId: TaskId) => {
            this.delegate!.deleteTask(taskId);
            this.loadTasksSilently();
        };

        const tasks = result.value;

        const rows = tasks.map((task) => {
            const taskId = task.id;
            return mapTaskToRow(
                task,
                () => startTask(taskId),
                () => stopTask(taskId),
                () => deleteTask(taskId)
            );
        });

        this.rows.set(rows);
        this.isLoading.set(false);
    };

    public load = async () => {
        this.isLoading.set(true);

        this.loadTasksSilently();
    };

    public onAddTask = () => {
        this.delegate!.runAddTaskFlow();
    };

    public reloadTasks = (silent: boolean): void => {
        if (silent) {
            this.loadTasksSilently();
        } else {
            this.load();
        }
    };
}
