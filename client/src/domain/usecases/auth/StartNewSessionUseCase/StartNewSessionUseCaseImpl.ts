import { AuthenticationServiceCheckToken } from "../../../interfaces/services/AuthenticationService";
import {
    AuthTokensRepositoryGet,
    AuthTokensRepositoryUpdate,
    AuthTokensRepositoryDelete,
} from "../../../repositories/AuthTokensRepository";
import { AuthStateRepositoryUpdate } from "../../../repositories/AuthStateRepository";

import StartNewSessionUseCase from "./StartNewSessionUseCase";
import AuthState from "../../../models/auth/AuthState";
import { ResultType } from "../../../../common/Result";

export type LocalTokensRepository = AuthTokensRepositoryUpdate &
    AuthTokensRepositoryGet &
    AuthTokensRepositoryDelete;

export default class StartNewSessionUseCaseImpl
    implements StartNewSessionUseCase
{
    constructor(
        private readonly authService: AuthenticationServiceCheckToken,
        private readonly localTokensRepository: LocalTokensRepository,
        private readonly authStateRepository: AuthStateRepositoryUpdate
    ) {}

    startNewSession = async (): Promise<void> => {
        this.authStateRepository.updateAuthState(AuthState.PROCESSING);

        const tokens = await this.localTokensRepository.getTokens();
        if (!tokens) {
            this.authStateRepository.updateAuthState(AuthState.LOGGED_OUT);
            return;
        }

        const isTokenValidResult = await this.authService.isTokenValid(
            tokens.accessToken
        );

        switch (isTokenValidResult.type) {
            case ResultType.Failure:
                this.authStateRepository.updateAuthState(AuthState.LOGGED_OUT);
                throw new Error("Unexpected error");

            case ResultType.Success:
                if (!isTokenValidResult.value) {
                    // TODO: refresh token
                    await this.localTokensRepository.deleteTokens();

                    this.authStateRepository.updateAuthState(
                        AuthState.LOGGED_OUT
                    );
                    return;
                }

                this.authStateRepository.updateAuthState(AuthState.LOGGED_IN);
        }
    };
}
