import { Result, ResultType } from "../../../common/Result";
import { TaskId } from "../../models/Task";
import {
    TasksRepositoryError,
    TasksRepositoryStop,
} from "../../repositories/TasksRepository";
import StopTaskUseCase, { StopTaskUseCaseError } from "./StopTaskUseCase";

export default class StopTaskUseCaseImpl implements StopTaskUseCase {
    private readonly tasksRepository: TasksRepositoryStop;

    constructor(tasksRepository: TasksRepositoryStop) {
        this.tasksRepository = tasksRepository;
    }

    execute = async (
        taskId: TaskId
    ): Promise<Result<undefined, StopTaskUseCaseError>> => {
        const result = await this.tasksRepository.stop(taskId);

        switch (result.type) {
            case ResultType.Success:
                return {
                    type: ResultType.Success,
                    value: undefined,
                };
            case ResultType.Failure:
                return {
                    type: ResultType.Failure,
                    error: this.mapError(result.error),
                };
        }
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
