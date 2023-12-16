import { Result } from "../../../common/Result";
import BacklogType from "../../models/BacklogType";
import Task from "../../models/Task";

export enum ListTasksInBacklogUseCaseError {
    InternalError = "INTERNAL_ERROR",
}

interface ListTasksInBacklogUseCase {
    execute(
        backlogTime: string
    ): Promise<Result<Task[], ListTasksInBacklogUseCaseError>>;
}

export default ListTasksInBacklogUseCase;
