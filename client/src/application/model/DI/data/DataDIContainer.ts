import AuthenticationServiceImpl from "../../../../data/services/AuthenticationServiceImpl";
import TasksRepositoryImpl from "../../../../data/repositories/TasksRepositoryImpl";
import { AuthStateRepository } from "../../../../domain/repositories/AuthStateRepository";
import AuthTokensRepository from "../../../../domain/repositories/AuthTokensRepository";

export default interface DataLayerDIContainer {
    getTasksRepository(): TasksRepositoryImpl;
    getAuthService(): AuthenticationServiceImpl;
    getLocalTokensRepository(): AuthTokensRepository;
    getAuthStateRepository(): AuthStateRepository;
}
