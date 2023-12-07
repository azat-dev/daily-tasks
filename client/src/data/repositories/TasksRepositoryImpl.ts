import { Result, ResultType } from "../../common/Result";
import BacklogType from "../../domain/models/BacklogType";
import Task, { TaskId } from "../../domain/models/Task";
import {
    TasksRepositoryList,
    TasksRepositoryStart,
    TasksRepositoryStop,
    TasksRepositoryError,
    TasksRepositoryDelete,
    TasksRepositoryAddNewTask,
} from "../../domain/repositories/TasksRepository";
import { DefaultApi, NewTaskData } from "../API";
import { TaskMapperDomain } from "./TaskMapper/TaskMapper";

export default class TasksRepositoryImpl
    implements
        TasksRepositoryList,
        TasksRepositoryStart,
        TasksRepositoryStop,
        TasksRepositoryDelete,
        TasksRepositoryAddNewTask
{
    private api: DefaultApi;
    private taskMapper: TaskMapperDomain;

    constructor(api: DefaultApi, taskMapper: TaskMapperDomain) {
        this.api = api;
        this.taskMapper = taskMapper;
    }

    getTasksInBacklog = async (
        backlogType: BacklogType,
        backlogDay: string
    ): Promise<Result<Task[], TasksRepositoryError>> => {
        try {
            const tasksDTO = await this.api.apiBacklogBacklogTypeDayTasksGet({
                backlogType: backlogType,
                day: backlogDay as any,
            });

            return {
                type: ResultType.Success,
                value: tasksDTO.map(this.taskMapper.toDomain),
            };
        } catch (error) {
            return {
                type: ResultType.Failure,
                error: TasksRepositoryError.InternalError,
            };
        }
    };

    start = async (
        taskId: TaskId
    ): Promise<Result<undefined, TasksRepositoryError>> => {
        try {
            await this.api.apiTasksTaskIdStartPost({
                taskId: taskId,
            });

            return {
                type: ResultType.Success,
                value: undefined,
            };
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            } else if (error?.status === 400) {
                err = TasksRepositoryError.TaskIsNotInCurrentDayBacklog;
            }

            return {
                type: ResultType.Failure,
                error: err,
            };
        }
    };

    stop = async (
        taskId: TaskId
    ): Promise<Result<undefined, TasksRepositoryError>> => {
        try {
            await this.api.apiTasksTaskIdStopPost({
                taskId: taskId,
            });

            return {
                type: ResultType.Success,
                value: undefined,
            };
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            } else if (error?.status === 400) {
                err = TasksRepositoryError.TaskIsNotInCurrentDayBacklog;
            }

            return {
                type: ResultType.Failure,
                error: err,
            };
        }
    };

    delete = async (
        taskId: TaskId
    ): Promise<Result<undefined, TasksRepositoryError>> => {
        try {
            await this.api.apiTasksTaskIdDelete({
                taskId: taskId,
            });

            return {
                type: ResultType.Success,
                value: undefined,
            };
        } catch (error: any) {
            let err = TasksRepositoryError.InternalError;

            if (error?.status === 404) {
                err = TasksRepositoryError.TaskNotFound;
            }

            return {
                type: ResultType.Failure,
                error: err,
            };
        }
    };

    addNewTask = async (
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

            const response = await this.api.apiBacklogBacklogTypeDayTasksPost({
                backlogType: backlogType,
                day: backlogDay as any,
                newTaskData,
            });

            return {
                type: ResultType.Success,
                value: response.taskId,
            };
        } catch (e) {
            return {
                type: ResultType.Failure,
                error: TasksRepositoryError.InternalError,
            };
        }
    };
}