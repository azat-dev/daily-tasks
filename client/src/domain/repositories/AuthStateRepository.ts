import AuthState from "../models/AuthState";

export interface AuthStateSubscription {
    cancel(): void;
}

export interface AuthStateRepositoryListen {
    listen(callback: (authState: AuthState) => void): AuthStateSubscription;
}

export interface AuthStateRepositoryGet {
    getCurrentAuthState(): Promise<AuthState>;
}

export interface AuthStateRepositoryUpdate {
    updateAuthState(authState: AuthState): Promise<void>;
}
