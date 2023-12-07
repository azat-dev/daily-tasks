import AuthState from "../../models/AuthState";
import { IAuthStateRepositoryListen } from "../../repositories/AuthStateRepository";
import { IListenAuthenticationStateUseCaseOutput } from "./ListenAuthenticationStateUseCase";

export default class ListenAuthenticationStateUseCaseImpl
    implements IListenAuthenticationStateUseCaseOutput
{
    private readonly authStateRepository: IAuthStateRepositoryListen;

    constructor(authStateRepository: IAuthStateRepositoryListen) {
        this.authStateRepository = authStateRepository;
    }

    listen = (callback: (state: AuthState) => void): any => {
        return this.authStateRepository.listen(callback);
    };
}
