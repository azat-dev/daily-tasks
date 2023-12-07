import { IAuthenticationServiceCheckToken } from "../../interfaces/services/AuthenticationService";
import {
    IAuthTokensRepositoryGet,
    IAuthTokensRepositoryUpdate,
    IAuthTokensRepositoryDelete,
} from "../../repositories/AuthTokensRepository";
import { IAuthStateRepositoryUpdate } from "../../repositories/AuthStateRepository";

import StartNewSessionUseCase from "./StartNewSessionUseCase";
import AuthState from "../../models/AuthState";
import { ResultType } from "../../../common/Result";

export type LocalTokensRepository = IAuthTokensRepositoryUpdate &
    IAuthTokensRepositoryGet &
    IAuthTokensRepositoryDelete;

export default class StartNewSessionUseCaseImpl
    implements StartNewSessionUseCase
{
    private readonly authService: IAuthenticationServiceCheckToken;
    private readonly localTokensRepository: LocalTokensRepository;
    private readonly authStateRepository: IAuthStateRepositoryUpdate;

    constructor(
        authService: IAuthenticationServiceCheckToken,
        localTokensRepository: LocalTokensRepository,
        authStateRepository: IAuthStateRepositoryUpdate
    ) {
        this.authService = authService;
        this.localTokensRepository = localTokensRepository;
        this.authStateRepository = authStateRepository;
    }

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
