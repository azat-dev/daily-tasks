namespace AuthTokensRepository {
    export enum AuthState {
        PROCESSING = "PROCESSING",
        LOGGED_IN = "LOGGED_IN",
        LOGGED_OUT = "LOGGED_OUT",
    }
}

export interface ITokensInfo {
    accessToken: string;
    refreshToken: string;
}

export interface IAuthTokensRepositoryUpdate {
    updateTokens(data: {
        accessToken: string;
        refreshToken: string;
    }): Promise<void>;
}

export interface IAuthTokensRepositoryDelete {
    deleteTokens(): Promise<void>;
}

export interface IAuthTokensRepositoryGet {
    getTokens(): Promise<ITokensInfo | null>;
}

export interface IAuthTokensRepositoryListen {
    listenChanges(callback: (currentTokens: ITokensInfo | null) => void): any;
}

export default AuthTokensRepository;
