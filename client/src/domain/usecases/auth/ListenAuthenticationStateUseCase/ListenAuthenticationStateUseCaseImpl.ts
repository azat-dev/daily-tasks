import AuthState from "../../../models/auth/AuthState";
import { AuthStateRepositoryListen } from "../../../repositories/AuthStateRepository";
import { ListenAuthenticationStateUseCaseOutput } from "./ListenAuthenticationStateUseCase";

export default class ListenAuthenticationStateUseCaseImpl
    implements ListenAuthenticationStateUseCaseOutput
{
    private readonly authStateRepository: AuthStateRepositoryListen;

    constructor(authStateRepository: AuthStateRepositoryListen) {
        this.authStateRepository = authStateRepository;
    }

    listen = (callback: (state: AuthState) => void): any => {
        return this.authStateRepository.listen(callback);
    };
}
