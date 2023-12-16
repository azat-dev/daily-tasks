import { ResultType } from "../../../../common/Result";
import DayPageViewModel, {
    DayPageViewViewModelDelegate,
} from "./DayPageViewModel";
import { TaskId } from "../../../../domain/models/Task";
import Value from "../../LogInPage/Value";
import { value } from "../../LogInPage/DefaultValue";
import RowViewModelImpl from "../Row/ViewModel/RowViewModelImpl";
import RowViewModel from "../Row/ViewModel/RowViewModel";
import TaskStatus from "../../../../domain/models/TaskStatus";

export default class DayPageViewModelImpl implements DayPageViewModel {
    // Properties
    public isLoading: Value<boolean> = value(true);
    public rows: Value<RowViewModel[]> = value([]);

    // Constructor

    constructor(public delegate: DayPageViewViewModelDelegate | null = null) {}

    // Methods

    public updateTaskStatus = (taskId: TaskId, status: TaskStatus) => {
        const row = this.rows.value.find((row) => row.key === String(taskId));

        if (!row) {
            return;
        }

        row.updateStatus(status);
    };

    private loadTasksSilently = async () => {
        const result = await this.delegate!.loadTasks();

        if (result.type !== ResultType.Success) {
            return;
        }

        const startTask = async (taskId: TaskId) => {
            const result = await this.delegate!.startTask(taskId);

            if (result.type !== ResultType.Success) {
                return;
            }

            this.updateTaskStatus(taskId, TaskStatus.InProgress);
        };

        const stopTask = (taskId: TaskId) => {
            this.delegate!.stopTask(taskId);
        };

        const deleteTask = (taskId: TaskId) => {
            this.delegate!.deleteTask(taskId);
            this.loadTasksSilently();
        };

        const doLaterWeek = (taskId: TaskId) => {};

        const doLaterMonth = (taskId: TaskId) => {};

        const tasks = result.value;

        const rows = tasks.map((task) => {
            const vm = new RowViewModelImpl(task);

            vm.delegate = {
                onStart: startTask,
                onStop: stopTask,
                onDelete: deleteTask,
                onDoLaterWeek: doLaterWeek,
                onDoLaterMonth: doLaterMonth,
            };

            return vm;
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
