import { useEffect, useState } from "react";
import ListTasksInBacklogUseCase from "../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import { ResultType } from "../../../common/Result";
import { DayPageViewViewModelRow } from "./DayPageViewModel";
import TaskStatus from "../../../domain/models/TaskStatus";
import StartTaskUseCase from "../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import Task from "../../../domain/models/Task";
import StopTaskUseCase from "../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import DeleteTaskUseCase from "../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import TaskPriority from "../../../domain/models/TaskPriority";

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
    startTaskUseCase: StartTaskUseCase,
    stopTaskUseCase: StopTaskUseCase,
    deleteTaskUseCase: DeleteTaskUseCase,
    reloadTasks: () => void
): DayPageViewViewModelRow => {
    const onStart = async () => {
        try {
            await startTaskUseCase.execute(task.id);
        } catch (error) {
            console.log(error);
        } finally {
            reloadTasks();
        }
    };

    const onStop = async () => {
        try {
            await stopTaskUseCase.execute(task.id);
        } catch (error) {
            console.log(error);
        } finally {
            reloadTasks();
        }
    };

    const onDelete = async () => {
        try {
            await deleteTaskUseCase.execute(task.id);
        } catch (error) {
            console.log(error);
        } finally {
            reloadTasks();
        }
    };

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

const useDayPageViewModel = (
    backlogDay: string,
    listCurrentDayTasks: ListTasksInBacklogUseCase,
    startTaskUseCase: StartTaskUseCase,
    stopTaskUseCase: StopTaskUseCase,
    deleteTaskUseCase: DeleteTaskUseCase
) => {
    const [isLoading, setIsLoading] = useState(true);
    const [rows, setRows] = useState<DayPageViewViewModelRow[]>([]);

    useEffect(() => {
        setIsLoading(true);

        const loadTasks = async () => {
            listCurrentDayTasks.execute(backlogDay).then((result) => {
                if (result.type !== ResultType.Success) {
                    return;
                }

                const tasks = result.value;
                const rows = tasks.map((task) =>
                    mapTaskToRow(
                        task,
                        startTaskUseCase,
                        stopTaskUseCase,
                        deleteTaskUseCase,
                        loadTasks
                    )
                );
                setRows(rows);
                setIsLoading(false);
            });
        };

        loadTasks();
    }, [listCurrentDayTasks, startTaskUseCase]);

    return {
        isLoading,
        rows,
    };
};

export default useDayPageViewModel;
