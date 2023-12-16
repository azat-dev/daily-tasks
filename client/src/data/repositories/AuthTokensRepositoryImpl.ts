import {
    IAuthTokensRepositoryUpdate,
    IAuthTokensRepositoryDelete,
    IAuthTokensRepositoryListen,
    ITokensInfo,
} from "../../domain/repositories/AuthTokensRepository";

export default class AuthTokensRepositoryImpl
    implements
        IAuthTokensRepositoryListen,
        IAuthTokensRepositoryUpdate,
        IAuthTokensRepositoryDelete
{
    private tokensKey = "tokens";

    private callbacks: Array<(tokens: ITokensInfo | null) => void> = [];

    getTokens = async (): Promise<ITokensInfo | null> => {
        const tokens = localStorage.getItem(this.tokensKey);
        if (!tokens) {
            return null;
        }
        return JSON.parse(tokens);
    };

    updateTokens = async (data: {
        accessToken: string;
        refreshToken: string;
    }): Promise<void> => {
        localStorage.setItem(this.tokensKey, JSON.stringify(data));

        const tokens = await this.getTokens();

        for (let callback of this.callbacks) {
            callback(tokens);
        }
    };

    deleteTokens = async (): Promise<void> => {
        localStorage.removeItem(this.tokensKey);
    };

    listenChanges = (
        callback: (currentTokens: ITokensInfo | null) => void
    ): any => {
        this.callbacks.push(callback);

        this.getTokens().then((tokens) => {
            for (let callback of this.callbacks) {
                callback(tokens);
            }
        });
    };
}
