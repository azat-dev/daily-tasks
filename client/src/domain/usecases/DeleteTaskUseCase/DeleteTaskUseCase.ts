import { Result } from "../../../common/Result";
import { TaskId } from "../../models/Task";

export enum DeleteTaskUseCaseError {
    InternalError = "INTERNAL_ERROR",
    TaskNotFound = "TASK_NOT_FOUND",
}

interface DeleteTaskUseCase {
    execute(taskId: TaskId): Promise<Result<undefined, DeleteTaskUseCaseError>>;
}

export default DeleteTaskUseCase;
