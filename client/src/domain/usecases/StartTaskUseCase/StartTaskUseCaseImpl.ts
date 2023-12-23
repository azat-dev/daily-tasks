import { Result } from "../../../common/Result";
import { TaskId } from "../../models/Task";
import {
    TasksRepositoryError,
    TasksRepositoryStart,
} from "../../repositories/TasksRepository";
import StartTaskUseCase, { StartTaskUseCaseError } from "./StartTaskUseCase";

export default class StartTaskUseCaseImpl implements StartTaskUseCase {
    constructor(private readonly tasksRepository: TasksRepositoryStart) {}

    execute = async (
        taskId: TaskId
    ): Promise<Result<Date, StartTaskUseCaseError>> => {
        const result = await this.tasksRepository.start(taskId);
        return Result.mapError(result, this.mapError);
    };

    private mapError = (error: TasksRepositoryError): StartTaskUseCaseError => {
        switch (error) {
            case TasksRepositoryError.InternalError:
                return StartTaskUseCaseError.InternalError;
            case TasksRepositoryError.TaskNotFound:
                return StartTaskUseCaseError.TaskNotFound;
            case TasksRepositoryError.TaskIsNotInCurrentDayBacklog:
                return StartTaskUseCaseError.TaskIsNotInCurrentDayBacklog;
        }
    };
}
