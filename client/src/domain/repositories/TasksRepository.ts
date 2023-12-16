import { Result } from "../../common/Result";
import BacklogType from "../models/BacklogType";
import NewTaskData from "../models/NewTaskData";
import Task, { TaskId } from "../models/Task";

export enum TasksRepositoryError {
    InternalError = "INTERNAL_ERROR",
    TaskNotFound = "TASK_NOT_FOUND",
    TaskIsNotInCurrentDayBacklog = "TASK_IS_NOT_IN_CURRENT_DAY_BACKLOG",
}

export interface TasksRepositoryList {
    getTasksInBacklog(
        backlogType: BacklogType,
        backlogDay: string
    ): Promise<Result<Task[], TasksRepositoryError>>;
}

export interface TasksRepositoryStart {
    start(taskId: TaskId): Promise<Result<Date, TasksRepositoryError>>;
}

export interface TasksRepositoryStop {
    stop(taskId: TaskId): Promise<Result<undefined, TasksRepositoryError>>;
}

export interface TasksRepositoryDelete {
    delete(taskId: TaskId): Promise<Result<undefined, TasksRepositoryError>>;
}

export interface TasksRepositoryAddNewTask {
    addNewTask(
        backlogType: BacklogType,
        backlogDay: string,
        data: NewTaskData
    ): Promise<Result<TaskId, TasksRepositoryError>>;
}
