import Task from "../../../domain/models/Task";
import TaskPriority from "../../../domain/models/TaskPriority";
import TaskStatus from "../../../domain/models/TaskStatus";
import { Task as TaskDTO } from "../../API";
import { TaskMapperDomain } from "./TaskMapper";

export default class TaskMapperImpl implements TaskMapperDomain {
    toDomain = (task: TaskDTO): Task => {
        return {
            id: task.id,
            title: task.title,
            createdAt: task.createdAt,
            updatedAt: task.updatedAt,
            description: task.description ?? "",
            status: task.status as TaskStatus,
            priority: task.priority as TaskPriority | undefined,
        };
    };
}
