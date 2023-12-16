import Task from "../../../domain/models/Task";
import { Task as TaskDTO } from "../../API/models/Task";

export interface TaskMapperDomain {
    toDomain(task: TaskDTO): Task;
}
