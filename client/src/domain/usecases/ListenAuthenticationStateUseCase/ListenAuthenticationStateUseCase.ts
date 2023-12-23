import AuthState from "../../models/AuthState";

export interface AuthStateSubscription {
    cancel: () => void;
}

export interface ListenAuthenticationStateUseCaseOutput {
    listen(callback: (state: AuthState) => void): AuthStateSubscription;
}
