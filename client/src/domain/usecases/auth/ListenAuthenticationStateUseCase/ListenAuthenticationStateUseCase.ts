import AuthState from "../../../models/auth/AuthState";

export interface AuthStateSubscription {
    cancel: () => void;
}

export interface ListenAuthenticationStateUseCaseOutput {
    listen(callback: (state: AuthState) => void): AuthStateSubscription;
}
