import { Result, ResultType } from "../../../common/Result";
import {
    AuthenticationServiceError,
    IAuthenticationServiceByUserNameAndPassword,
} from "../../interfaces/services/AuthenticationService";
import AuthState from "../../models/AuthState";
import { IAuthStateRepositoryUpdate } from "../../repositories/AuthStateRepository";
import { IAuthTokensRepositoryUpdate } from "../../repositories/AuthTokensRepository";
import {
    ILogInByUserNameAndPasswordUseCase,
    LoginByUserNamedAndPasswordUseCaseError,
} from "./LogInByUserNameAndPasswordUseCase";

export default class LogInByUserNameAndPasswordUseCaseImpl
    implements ILogInByUserNameAndPasswordUseCase
{
    constructor(
        private readonly authService: IAuthenticationServiceByUserNameAndPassword,
        private readonly localTokensRepository: IAuthTokensRepositoryUpdate,
        private readonly authStateRepository: IAuthStateRepositoryUpdate
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
