import { Result, ResultType } from "../../../common/Result";
import BacklogType from "../../models/BacklogType";
import Task from "../../models/Task";
import TaskPriority from "../../models/TaskPriority";
import TaskStatus from "../../models/TaskStatus";
import {
    TasksRepositoryError,
    TasksRepositoryList,
} from "../../repositories/TasksRepository";
import ListTasksInBacklogUseCaseImpl from "./ListTasksInBacklogUseCaseImpl";

const makeSut = (backlogType: BacklogType) => {
    const tasksRepository: TasksRepositoryList = {
        getTasksInBacklog: jest.fn(() => {
            throw new Error("Not implemented");
        }),
    };

    const useCase = new ListTasksInBacklogUseCaseImpl(
        backlogType,
        tasksRepository
    );

    return {
        useCase,
        tasksRepository,
    };
};

const anyTasks = (): Task[] => {
    return [
        {
            id: 1,
            title: "task 1",
            createdAt: new Date(),
            updatedAt: new Date(),
            description: "description 1",
            status: TaskStatus.NotStarted,
            priority: TaskPriority.LOW,
        },
        {
            id: 2,
            title: "task 2",
            createdAt: new Date(),
            updatedAt: new Date(),
            description: "description 2",
            status: TaskStatus.NotStarted,
            priority: TaskPriority.LOW,
        },
    ];
};

describe("ListTasksInBacklogUseCaseImpl", () => {
    test("Should return tasks", async () => {
        // Given
        const backlogType = BacklogType.Week;
        const backlogDay = new Date().toISOString().split("T")[0];
        const { useCase, tasksRepository } = makeSut(backlogType);
        const expectedTasks = anyTasks();

        tasksRepository.getTasksInBacklog = jest.fn(
            async (
                backlogType: BacklogType,
                backlogDay: string
            ): Promise<Result<Task[], TasksRepositoryError>> => {
                return {
                    type: ResultType.Success,
                    value: expectedTasks,
                };
            }
        );

        // When
        const result = await useCase.execute(backlogDay);

        // Then
        expect(tasksRepository.getTasksInBacklog).toBeCalledWith(
            backlogType,
            backlogDay
        );
        expect(result).toEqual({
            type: ResultType.Success,
            value: expectedTasks,
        });
    });
});
