import { Configuration, DefaultApi } from "../../../../data/API";
import AuthStateRepositoryImpl from "../../../../data/repositories/AuthStateRepositoryImpl";
import AuthTokensRepositoryImpl from "../../../../data/repositories/AuthTokensRepositoryImpl";
import TaskMapperImpl from "../../../../data/repositories/TaskMapper/TaskMapperImpl";
import TasksRepositoryImpl from "../../../../data/repositories/TasksRepositoryImpl";
import AuthenticationServiceImpl from "../../../../data/services/AuthenticationServiceImpl";
import { AuthStateRepository } from "../../../../domain/repositories/AuthStateRepository";
import AuthTokensRepository from "../../../../domain/repositories/AuthTokensRepository";
import DataLayerDIContainer from "./DataDIContainer";

export default class DataDIContainerImpl implements DataLayerDIContainer {
    private apiClient: DefaultApi;

    constructor(
        private basePath: string,
        private localTokensRepository: AuthTokensRepository = new AuthTokensRepositoryImpl(),
        private authStateRepository: AuthStateRepository = new AuthStateRepositoryImpl()
    ) {
        this.apiClient = this.makeApiClient();
    }

    private makeApiClient = () => {
        return new DefaultApi(
            new Configuration({
                basePath: this.basePath,
                accessToken: async () => {
                    const tokens = await this.localTokensRepository.getTokens();
                    return tokens?.accessToken ?? "";
                },
            })
        );
    };

    public getApiClient = () => {
        return this.apiClient;
    };

    public getAuthService = () => {
        return new AuthenticationServiceImpl(this.getApiClient());
    };

    public getTasksRepository = () => {
        return new TasksRepositoryImpl(
            this.getApiClient(),
            new TaskMapperImpl()
        );
    };

    public getLocalTokensRepository = () => {
        return this.localTokensRepository;
    };

    public getAuthStateRepository = () => {
        return this.authStateRepository;
    };
}
