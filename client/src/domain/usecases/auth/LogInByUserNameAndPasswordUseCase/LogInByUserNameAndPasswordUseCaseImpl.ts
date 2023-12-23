import { Result, ResultType } from "../../../../common/Result";
import {
    AuthenticationServiceError,
    AuthenticationServiceByUserNameAndPassword,
} from "../../../interfaces/services/AuthenticationService";
import AuthState from "../../../models/auth/AuthState";
import { AuthStateRepositoryUpdate } from "../../../repositories/AuthStateRepository";
import { AuthTokensRepositoryUpdate } from "../../../repositories/AuthTokensRepository";
import {
    LogInByUserNameAndPasswordUseCase,
    LoginByUserNamedAndPasswordUseCaseError,
} from "./LogInByUserNameAndPasswordUseCase";

export default class LogInByUserNameAndPasswordUseCaseImpl
    implements LogInByUserNameAndPasswordUseCase
{
    constructor(
        private readonly authService: AuthenticationServiceByUserNameAndPassword,
        private readonly localTokensRepository: AuthTokensRepositoryUpdate,
        private readonly authStateRepository: AuthStateRepositoryUpdate
    ) {
        this.authService = authService;
        this.localTokensRepository = localTokensRepository;
        this.authStateRepository = authStateRepository;
    }

    logInByUserName = async (
        username: string,
        password: string
    ): Promise<Result<undefined, LoginByUserNamedAndPasswordUseCaseError>> => {
        const result = await this.authService.authenticateByUserName(
            username,
            password
        );

        switch (result.type) {
            case ResultType.Success:
                const { accessToken, refreshToken } = result.value;

                this.localTokensRepository.updateTokens({
                    accessToken: accessToken,
                    refreshToken: refreshToken,
                });

                this.authStateRepository.updateAuthState(AuthState.LOGGED_IN);

                return Result.success(undefined);

            case ResultType.Failure:
                switch (result.error) {
                    case AuthenticationServiceError.InternalError:
                        return Result.failure(
                            LoginByUserNamedAndPasswordUseCaseError.InternalError
                        );
                    case AuthenticationServiceError.WrongCredentials:
                        return Result.failure(
                            LoginByUserNamedAndPasswordUseCaseError.WrongCredentials
                        );
                }
        }
    };
}
