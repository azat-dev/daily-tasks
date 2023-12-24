import BacklogType from "../../../../domain/models/BacklogType";
import AddNewTaskUseCaseImpl from "../../../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import ListenAuthenticationStateUseCaseImpl from "../../../../domain/usecases/auth/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCaseImpl";
import LogInByUserNameAndPasswordUseCaseImpl from "../../../../domain/usecases/auth/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCaseImpl";
import StartNewSessionUseCaseImpl from "../../../../domain/usecases/auth/StartNewSessionUseCase/StartNewSessionUseCaseImpl";
import DeleteTaskUseCase from "../../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCase";
import DeleteTaskUseCaseImpl from "../../../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import EditTaskUseCaseImpl from "../../../../domain/usecases/EditTaskUseCase/AddNewTaskUseCaseImpl";
import ListTasksInBacklogUseCase from "../../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCase";
import ListTasksInBacklogUseCaseImpl from "../../../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import LoadTaskUseCaseImpl from "../../../../domain/usecases/LoadTaskUseCase/LoadTaskUseCaseImpl";
import StartTaskUseCase from "../../../../domain/usecases/StartTaskUseCase/StartTaskUseCase";
import StartTaskUseCaseImpl from "../../../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCase from "../../../../domain/usecases/StopTaskUseCase/StopTaskUseCase";
import StopTaskUseCaseImpl from "../../../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";

import DataLayerDIContainer from "../data/DataDIContainer";

export default class DomainDIContainer {
    constructor(private data: DataLayerDIContainer) {}

    getStartNewSessionUseCase = () => {
        return new StartNewSessionUseCaseImpl(
            this.data.getAuthService(),
            this.data.getLocalTokensRepository(),
            this.data.getAuthStateRepository()
        );
    };

    getLogInByUserNameAndPasswordUseCase = () => {
        return new LogInByUserNameAndPasswordUseCaseImpl(
            this.data.getAuthService(),
            this.data.getLocalTokensRepository(),
            this.data.getAuthStateRepository()
        );
    };

    getListenAuthenticationStateUseCase = () => {
        return new ListenAuthenticationStateUseCaseImpl(
            this.data.getAuthStateRepository()
        );
    };

    getLoadTaskUseCase = () => {
        return new LoadTaskUseCaseImpl(this.data.getTasksRepository());
    };

    getEditTaskUseCase = () => {
        return new EditTaskUseCaseImpl(this.data.getTasksRepository());
    };

    getStartTaskUseCase = (): StartTaskUseCase => {
        return new StartTaskUseCaseImpl(this.data.getTasksRepository());
    };

    getStopTaskUseCase = (): StopTaskUseCase => {
        return new StopTaskUseCaseImpl(this.data.getTasksRepository());
    };

    getDeleteTaskUseCase = (): DeleteTaskUseCase => {
        return new DeleteTaskUseCaseImpl(this.data.getTasksRepository());
    };

    getListTasksInBacklogUseCase = (
        backlogType: BacklogType
    ): ListTasksInBacklogUseCase => {
        return new ListTasksInBacklogUseCaseImpl(
            backlogType,
            this.data.getTasksRepository()
        );
    };

    getAddNewTaskUseCase = () => {
        return new AddNewTaskUseCaseImpl(this.data.getTasksRepository());
    };
}
