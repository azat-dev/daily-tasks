import AuthState from "../../../models/auth/AuthState";
import { AuthStateRepositoryListen } from "../../../repositories/AuthStateRepository";
import { ListenAuthenticationStateUseCaseOutput } from "./ListenAuthenticationStateUseCase";

export default class ListenAuthenticationStateUseCaseImpl
    implements ListenAuthenticationStateUseCaseOutput
{
    constructor(
        private readonly authStateRepository: AuthStateRepositoryListen
    ) {}

    public listen = (callback: (state: AuthState) => void): any => {
        return this.authStateRepository.listen(callback);
    };
}
