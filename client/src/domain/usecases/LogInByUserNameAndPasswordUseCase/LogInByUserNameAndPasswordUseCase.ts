import { Result } from "../../../common/Result";

export enum LoginByUserNamedAndPasswordUseCaseError {
    WrongCredentials = "WrongCredentials",
    InternalError = "InternalError",
}

export interface ILogInByUserNameAndPasswordUseCase {
    logInByUserName(
        username: string,
        password: string
    ): Promise<Result<undefined, LoginByUserNamedAndPasswordUseCaseError>>;
}
