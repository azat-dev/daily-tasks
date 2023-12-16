import AuthState from "../models/AuthState";

export interface IAuthStateSubscription {
    cancel(): void;
}

export interface IAuthStateRepositoryListen {
    listen(callback: (authState: AuthState) => void): IAuthStateSubscription;
}

export interface IAuthStateRepositoryGet {
    getCurrentAuthState(): Promise<AuthState>;
}

export interface IAuthStateRepositoryUpdate {
    updateAuthState(authState: AuthState): Promise<void>;
}
