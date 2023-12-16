import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../models/Task";
import {
    TasksRepositoryError,
    TasksRepositoryStart,
} from "../../repositories/TasksRepository";
import StartTaskUseCase, { StartTaskUseCaseError } from "./StartTaskUseCase";

export default class StartTaskUseCaseImpl implements StartTaskUseCase {
    private readonly tasksRepository: TasksRepositoryStart;

    constructor(tasksRepository: TasksRepositoryStart) {
        this.tasksRepository = tasksRepository;
    }

    execute = async (
        taskId: TaskId
    ): Promise<Result<undefined, StartTaskUseCaseError>> => {
        const result = await this.tasksRepository.start(taskId);

        switch (result.type) {
            case ResultType.Success:
                return Result.success(undefined);
            case ResultType.Failure:
                return Result.failure(this.mapError(result.error));
        }
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
