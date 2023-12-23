import { Result, ResultType } from "../../../common/Result";
import Task from "../../models/Task";
import { TasksRepositoryGet } from "../../repositories/TasksRepository";
import { LoadTaskUseCase, LoadTaskUseCaseError } from "./LoadTaskUseCase";

export default class LoadTaskUseCaseImpl implements LoadTaskUseCase {
    public constructor(private readonly tasksRepository: TasksRepositoryGet) {}

    execute = async (
        taskId: number
    ): Promise<Result<Task | null, LoadTaskUseCaseError>> => {
        const result = await this.tasksRepository.get(taskId);

        if (result.type === ResultType.Failure) {
            return Result.failure(LoadTaskUseCaseError.TaskNotFound);
        }

        return Result.success(result.value);
    };
}
