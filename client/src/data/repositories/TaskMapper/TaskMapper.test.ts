import TaskPriority from "../../../domain/models/TaskPriority";
import TaskStatus from "../../../domain/models/TaskStatus";
import { Task as TaskDTO, TaskPriorityEnum, TaskStatusEnum } from "../../API";
import TaskMapperImpl from "./TaskMapperImpl";

const createSut = () => {
    return new TaskMapperImpl();
};

describe("TaskMapper", () => {
    test("Should map a task", () => {
        // Given
        const sut = createSut();

        const task: TaskDTO = {
            id: 1,
            title: "task 1",
            createdAt: new Date(),
            updatedAt: new Date(),
            description: "description 1",
            status: TaskStatusEnum.NotStarted,
            priority: "high",
        };

        // When
        const mappedTask = sut.toDomain(task);

        // Then
        expect(mappedTask).toEqual({
            id: 1,
            title: "task 1",
            createdAt: task.createdAt,
            updatedAt: task.updatedAt,
            description: task.description,
            status: TaskStatus.NotStarted,
            priority: "high",
        });
    });

    test("Should map a task without description", () => {
        // Given
        const sut = createSut();
        const task = {
            id: 1,
            title: "task 1",
            createdAt: new Date(),
            updatedAt: new Date(),
            status: TaskStatusEnum.NotStarted,
            priority: TaskPriorityEnum.Low,
        };

        // When
        const mappedTask = sut.toDomain(task);

        // Then
        expect(mappedTask).toEqual({
            id: 1,
            title: "task 1",
            createdAt: task.createdAt,
            updatedAt: task.updatedAt,
            description: "",
            status: TaskStatus.NotStarted,
            priority: TaskPriority.LOW,
        });
    });
});
