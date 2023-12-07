import TaskStatus from "./TaskStatus";

export type TaskId = number;

interface Task {
    id: TaskId;
    title: string;
    description: string;
    status: TaskStatus;
    priority: number | undefined;
    createdAt: Date;
    updatedAt: Date;
}

export default Task;
