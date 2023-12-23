import { Result, ResultType } from "../../../common/Result";
import BacklogType from "../../models/BacklogType";
import Task from "../../models/Task";
import { TasksRepositoryList } from "../../repositories/TasksRepository";
import ListTasksInBacklogUseCase, {
    ListTasksInBacklogUseCaseError,
} from "./ListTasksInBacklogUseCase";

export default class ListCurrentTasksUseCaseImpl
    implements ListTasksInBacklogUseCase
{
    constructor(
        private readonly backlogType: BacklogType,
        private readonly tasksRepository: TasksRepositoryList
    ) {}

    execute = async (
        backlogDay: string
    ): Promise<Result<Task[], ListTasksInBacklogUseCaseError>> => {
        const result = await this.tasksRepository.getTasksInBacklog(
            this.backlogType,
            backlogDay
        );

        switch (result.type) {
            case ResultType.Success:
                return result;
            case ResultType.Failure:
                return Result.failure(
                    ListTasksInBacklogUseCaseError.InternalError
                );
        }
    };
}
