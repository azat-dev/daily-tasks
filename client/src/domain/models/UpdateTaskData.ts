import TaskPriority from "./TaskPriority";

export default interface UpdateTaskData {
    title: string;
    description?: string;
    priority?: TaskPriority | null;
}
