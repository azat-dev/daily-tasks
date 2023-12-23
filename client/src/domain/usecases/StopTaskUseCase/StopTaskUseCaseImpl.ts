import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../models/Task";
import {
    TasksRepositoryError,
    TasksRepositoryStop,
} from "../../repositories/TasksRepository";
import StopTaskUseCase, { StopTaskUseCaseError } from "./StopTaskUseCase";

export default class StopTaskUseCaseImpl implements StopTaskUseCase {
    constructor(private readonly tasksRepository: TasksRepositoryStop) {}

    execute = async (
        taskId: TaskId
    ): Promise<Result<Date, StopTaskUseCaseError>> => {
        const result = await this.tasksRepository.stop(taskId);
        return Result.mapError(result, this.mapError);
    };

    private mapError = (error: TasksRepositoryError): StopTaskUseCaseError => {
        switch (error) {
            case TasksRepositoryError.InternalError:
                return StopTaskUseCaseError.InternalError;
            case TasksRepositoryError.TaskNotFound:
                return StopTaskUseCaseError.TaskNotFound;
            case TasksRepositoryError.TaskIsNotInCurrentDayBacklog:
                return StopTaskUseCaseError.TaskIsNotInCurrentDayBacklog;
        }
    };
}
