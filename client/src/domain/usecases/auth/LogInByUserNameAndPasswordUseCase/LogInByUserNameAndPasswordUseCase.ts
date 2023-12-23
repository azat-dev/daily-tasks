import { Result } from "../../../../common/Result";

export enum LoginByUserNamedAndPasswordUseCaseError {
    WrongCredentials = "WrongCredentials",
    InternalError = "InternalError",
}

export interface LogInByUserNameAndPasswordUseCase {
    execute(
        username: string,
        password: string
    ): Promise<Result<undefined, LoginByUserNamedAndPasswordUseCaseError>>;
}
