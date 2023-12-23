namespace AuthTokensRepository {
    export enum AuthState {
        PROCESSING = "PROCESSING",
        LOGGED_IN = "LOGGED_IN",
        LOGGED_OUT = "LOGGED_OUT",
    }
}

export interface TokensInfo {
    accessToken: string;
    refreshToken: string;
}

export interface AuthTokensRepositoryUpdate {
    updateTokens(data: {
        accessToken: string;
        refreshToken: string;
    }): Promise<void>;
}

export interface AuthTokensRepositoryDelete {
    deleteTokens(): Promise<void>;
}

export interface AuthTokensRepositoryGet {
    getTokens(): Promise<TokensInfo | null>;
}

export interface AuthTokensRepositoryListen {
    listenChanges(callback: (currentTokens: TokensInfo | null) => void): any;
}

export default AuthTokensRepository;
