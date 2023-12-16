import TaskPriority from "./TaskPriority";
import TaskStatus from "./TaskStatus";

export type TaskId = number;

interface Task {
    id: TaskId;
    title: string;
    description: string;
    status: TaskStatus;
    priority: TaskPriority | undefined;
    createdAt: Date;
    updatedAt: Date;
}

export default Task;
