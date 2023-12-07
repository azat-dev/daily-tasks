import { Result, ResultType } from "../../../common/Result";
import BacklogType from "../../models/BacklogType";
import NewTaskData from "../../models/NewTaskData";
import { TaskId } from "../../models/Task";
import { TasksRepositoryAddNewTask } from "../../repositories/TasksRepository";
import AddNewTaskUseCase, { AddNewTaskUseCaseError } from "./AddNewTaskUseCase";

export default class AddNewTaskUseCaseImpl implements AddNewTaskUseCase {
    private readonly tasksRepository: TasksRepositoryAddNewTask;

    constructor(tasksRepository: TasksRepositoryAddNewTask) {
        this.tasksRepository = tasksRepository;
    }

    execute = async (
        backlogType: BacklogType,
        backlogDay: string,
        data: NewTaskData
    ): Promise<Result<TaskId, AddNewTaskUseCaseError>> => {
        const result = await this.tasksRepository.addNewTask(
            backlogType,
            backlogDay,
            data
        );

        switch (result.type) {
            case ResultType.Success:
                return result;
            case ResultType.Failure:
                return {
                    type: ResultType.Failure,
                    error: AddNewTaskUseCaseError.InternalError,
                };
        }
    };
}
