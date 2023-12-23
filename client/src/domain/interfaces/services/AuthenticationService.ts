import { Result } from "../../../common/Result";

export enum AuthenticationServiceError {
    InternalError = "InternalError",
    WrongCredentials = "WrongCredentials",
}

export interface AuthenticationTokenPair {
    accessToken: string;
    refreshToken: string;
}

export interface AuthenticationServiceByUserNameAndPassword {
    authenticateByUserName(
        username: string,
        password: string
    ): Promise<Result<AuthenticationTokenPair, AuthenticationServiceError>>;
}

export interface AuthenticationServiceRefreshToken {
    refreshToken(
        refreshToken: string
    ): Promise<Result<AuthenticationTokenPair, AuthenticationServiceError>>;
}

export interface AuthenticationServiceCheckToken {
    isTokenValid(
        refreshToken: string
    ): Promise<Result<boolean, AuthenticationServiceError>>;
}
