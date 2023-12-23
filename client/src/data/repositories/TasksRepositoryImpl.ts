import { Result } from "../../common/Result";
import BacklogType from "../../domain/models/BacklogType";
import Task, { TaskId } from "../../domain/models/Task";
import UpdateTaskData from "../../domain/models/UpdateTaskData";
import {
    TasksRepositoryList,
    TasksRepositoryStart,
    TasksRepositoryStop,
    TasksRepositoryError,
    TasksRepositoryDelete,
    TasksRepositoryAddNewTask,
    TasksRepositoryGet,
    TasksRepositoryUpdate,
} from "../../domain/repositories/TasksRepository";
import { DefaultApi, NewTaskData } from "../API";
import { TaskMapperDomain } from "./TaskMapper/TaskMapper";

export default class TasksRepositoryImpl
    implements
        TasksRepositoryList,
        TasksRepositoryStart,
        TasksRepositoryStop,
        TasksRepositoryDelete,
        TasksRepositoryAddNewTask,
        TasksRepositoryGet,
        TasksRepositoryUpdate
{
    constructor(
        private readonly api: DefaultApi,
        private readonly taskMapper: TaskMapperDomain
    ) {}

    getTasksInBacklog = async (
        backlogType: BacklogType,
        backlogDay: string
    ): Promise<Result<Task[], TasksRepositoryError>> => {
        try {
            const tasksDTO =
                await this.api.apiWithAuthTasksBacklogBacklogTypeForDayGet({
                    backlogType: backlogType,
                    day: backlogDay as any,
                });

            return Result.success(tasksDTO.map(this.taskMapper.toDomain));
        } catch (error) {
            return Result.failure(TasksRepositoryError.InternalError);
        }
    };

    public start = async (
        taskId: TaskId
    ): Promise<Result<Date, TasksRepositoryError>> => {
        try {
            const response = await this.api.apiWithAuthTasksTaskIdStartPost({
                taskId: taskId,
            });

            return Result.success(response.startedAt);
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            } else if (error?.status === 400) {
                err = TasksRepositoryError.TaskIsNotInCurrentDayBacklog;
            }

            return Result.failure(err);
        }
    };

    public stop = async (
        taskId: TaskId
    ): Promise<Result<Date, TasksRepositoryError>> => {
        try {
            const result = await this.api.apiWithAuthTasksTaskIdStopPost({
                taskId: taskId,
            });

            return Result.success(result.stoppedAt);
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            } else if (error?.status === 400) {
                err = TasksRepositoryError.TaskIsNotInCurrentDayBacklog;
            }

            return Result.failure(err);
        }
    };

    public delete = async (
        taskId: TaskId
    ): Promise<Result<undefined, TasksRepositoryError>> => {
        try {
            await this.api.apiWithAuthTasksTaskIdDelete({
                taskId: taskId,
            });

            return Result.success(undefined);
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            }

            return Result.failure(err);
        }
    };

    public addNewTask = async (
        backlogType: BacklogType,
        backlogDay: string,
        data: NewTaskData
    ): Promise<Result<TaskId, TasksRepositoryError>> => {
        try {
            let cleanedDescription = (data.description ?? "").trim();

            const newTaskData = {
                title: data.title,
                description: cleanedDescription,
                priority: data.priority,
            };

            if (cleanedDescription) {
                newTaskData.description = cleanedDescription;
            }

            const response =
                await this.api.apiWithAuthTasksBacklogBacklogTypeForDayPost({
                    backlogType: backlogType,
                    day: backlogDay as any,
                    newTaskData,
                });

            return Result.success(response.taskId);
        } catch (e) {
            return Result.failure(TasksRepositoryError.InternalError);
        }
    };

    public updateTask = async (
        taskId: TaskId,
        data: UpdateTaskData
    ): Promise<Result<Task, TasksRepositoryError>> => {
        try {
            let cleanedDescription = (data.description ?? "").trim();

            const updateTaskData = {
                title: data.title,
                description: cleanedDescription,
                priority: data.priority as any,
            };

            if (cleanedDescription) {
                updateTaskData.description = cleanedDescription;
            }

            const response = await this.api.apiWithAuthTasksTaskIdPost({
                taskId,
                updateTaskData: updateTaskData,
            });

            return Result.success(this.taskMapper.toDomain(response));
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            }

            return Result.failure(err);
        }
    };

    public get = async (
        taskId: number
    ): Promise<Result<Task | null, TasksRepositoryError>> => {
        try {
            const response = await this.api.apiWithAuthTasksTaskIdGet({
                taskId: taskId,
            });

            return Result.success(this.taskMapper.toDomain(response));
        } catch (error: any) {
            if (error?.response?.status === 404) {
                return Result.success(null);
            }

            return Result.failure(TasksRepositoryError.InternalError);
        }
    };
}
