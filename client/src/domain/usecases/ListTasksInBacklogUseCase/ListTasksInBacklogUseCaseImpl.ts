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
    private backlogType: BacklogType;
    private tasksRepository: TasksRepositoryList;

    constructor(
        backlogType: BacklogType,
        tasksRepository: TasksRepositoryList
    ) {
        this.backlogType = backlogType;
        this.tasksRepository = tasksRepository;
    }

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
                return {
                    type: ResultType.Failure,
                    error: ListTasksInBacklogUseCaseError.InternalError,
                };
        }
    };
}
