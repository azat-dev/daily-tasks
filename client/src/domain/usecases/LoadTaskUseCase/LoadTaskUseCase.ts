import { Result } from "../../../common/Result";
import Task, { TaskId } from "../../models/Task";

export enum LoadTaskUseCaseError {
    TaskNotFound = "TaskNotFound",
    InternalError = "InternalError",
}

export interface ILoadTaskUseCase {
    execute(taskId: TaskId): Promise<Result<Task | null, LoadTaskUseCaseError>>;
}
