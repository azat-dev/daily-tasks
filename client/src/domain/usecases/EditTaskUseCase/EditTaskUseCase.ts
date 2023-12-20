import { Result } from "../../../common/Result";
import UpdateTaskData from "../../models/UpdateTaskData";
import Task, { TaskId } from "../../models/Task";

export enum EditTaskUseCaseError {
    InternalError = "INTERNAL_ERROR",
}

interface EditTaskUseCase {
    execute(
        taskId: TaskId,
        data: UpdateTaskData
    ): Promise<Result<Task, EditTaskUseCaseError>>;
}

export default EditTaskUseCase;
