import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../models/Task";
import {
    TasksRepositoryError,
    TasksRepositoryDelete,
} from "../../repositories/TasksRepository";
import DeleteTaskUseCase, { DeleteTaskUseCaseError } from "./DeleteTaskUseCase";

export default class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {
    constructor(private readonly tasksRepository: TasksRepositoryDelete) {}

    execute = async (
        taskId: TaskId
    ): Promise<Result<undefined, DeleteTaskUseCaseError>> => {
        const result = await this.tasksRepository.delete(taskId);

        switch (result.type) {
            case ResultType.Success:
                return Result.success(undefined);
            case ResultType.Failure:
                return Result.failure(this.mapError(result.error));
        }
    };

    private mapError = (
        error: TasksRepositoryError
    ): DeleteTaskUseCaseError => {
        switch (error) {
            case TasksRepositoryError.InternalError:
                return DeleteTaskUseCaseError.InternalError;
            case TasksRepositoryError.TaskNotFound:
                return DeleteTaskUseCaseError.TaskNotFound;
            default:
                return DeleteTaskUseCaseError.InternalError;
        }
    };
}
