import { Result, ResultType } from "../../../common/Result";
import Task, { TaskId } from "../../models/Task";
import UpdateTaskData from "../../models/UpdateTaskData";
import { TasksRepositoryUpdate } from "../../repositories/TasksRepository";
import EditTaskUseCase, { EditTaskUseCaseError } from "./EditTaskUseCase";

export default class EditTaskUseCaseImpl implements EditTaskUseCase {
    public constructor(
        private readonly tasksRepository: TasksRepositoryUpdate
    ) {}

    public execute = async (
        taskId: TaskId,
        data: UpdateTaskData
    ): Promise<Result<Task, EditTaskUseCaseError>> => {
        const result = await this.tasksRepository.updateTask(taskId, data);

        switch (result.type) {
            case ResultType.Success:
                return result;
            case ResultType.Failure:
                return Result.failure(EditTaskUseCaseError.InternalError);
        }
    };
}
