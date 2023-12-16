import AuthState from "../../domain/models/AuthState";
import {
    IAuthStateRepositoryGet,
    IAuthStateRepositoryListen,
    IAuthStateRepositoryUpdate,
} from "../../domain/repositories/AuthStateRepository";

export default class AuthStateRepositoryImpl
    implements
        IAuthStateRepositoryListen,
        IAuthStateRepositoryGet,
        IAuthStateRepositoryUpdate
{
    private readonly subscribers: ((authState: AuthState) => void)[] = [];
    private authState: AuthState = AuthState.LOGGED_OUT;

    getCurrentAuthState = async (): Promise<AuthState> => {
        return this.authState;
    };

    listen = (
        callback: (authState: AuthState) => void
    ): { cancel: () => void } => {
        this.subscribers.push(callback);
        return {
            cancel: () => {
                const index = this.subscribers.indexOf(callback);
                if (index > -1) {
                    this.subscribers.splice(index, 1);
                }
            },
        };
    };

    updateAuthState = async (authState: AuthState): Promise<void> => {
        this.authState = authState;
        this.subscribers.forEach((listener) => listener(authState));
    };
}
