import { Result } from "../../../common/Result";
import { TaskId } from "../../models/Task";

export enum StopTaskUseCaseError {
    InternalError = "INTERNAL_ERROR",
    TaskNotFound = "TASK_NOT_FOUND",
    TaskIsNotInCurrentDayBacklog = "TASK_IS_NOT_IN_CURRENT_DAY_BACKLOG",
}

interface StopTaskUseCase {
    execute(taskId: TaskId): Promise<Result<Date, StopTaskUseCaseError>>;
}

export default StopTaskUseCase;
