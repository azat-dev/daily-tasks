import { TaskId } from "./Task";
import TaskPriority from "./TaskPriority";
import TaskStatus from "./TaskStatus";

export default interface UpdateTaskData {
    title?: string;
    description?: string;
    priority?: TaskPriority | null;
}
