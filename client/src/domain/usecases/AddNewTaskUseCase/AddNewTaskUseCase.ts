import { Result } from "../../../common/Result";
import BacklogType from "../../models/BacklogType";
import NewTaskData from "../../models/NewTaskData";
import { TaskId } from "../../models/Task";

export enum AddNewTaskUseCaseError {
    InternalError = "INTERNAL_ERROR",
}

interface AddNewTaskUseCase {
    execute(
        backlogType: BacklogType,
        backlogDay: string,
        data: NewTaskData
    ): Promise<Result<TaskId, AddNewTaskUseCaseError>>;
}

export default AddNewTaskUseCase;
