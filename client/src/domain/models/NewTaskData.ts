import TaskPriority from "./TaskPriority";

interface NewTaskData {
    title: string;
    description: string | undefined;
    priority: TaskPriority | undefined;
}

export default NewTaskData;
