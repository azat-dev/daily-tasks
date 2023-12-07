import { Result } from "../../../common/Result";

export enum AuthenticationServiceError {
    InternalError = "InternalError",
    WrongCredentials = "WrongCredentials",
}

export interface IAuthenticationTokenPair {
    accessToken: string;
    refreshToken: string;
}

export interface IAuthenticationServiceByUserNameAndPassword {
    authenticateByUserName(
        username: string,
        password: string
    ): Promise<Result<IAuthenticationTokenPair, AuthenticationServiceError>>;
}

export interface IAuthenticationServiceRefreshToken {
    refreshToken(
        refreshToken: string
    ): Promise<Result<IAuthenticationTokenPair, AuthenticationServiceError>>;
}

export interface IAuthenticationServiceCheckToken {
    isTokenValid(
        refreshToken: string
    ): Promise<Result<boolean, AuthenticationServiceError>>;
}
