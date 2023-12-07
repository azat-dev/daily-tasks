import AuthState from "../../models/AuthState";

export interface AuthStateSubscription {
    cancel: () => void;
}

export interface IListenAuthenticationStateUseCaseOutput {
    listen(callback: (state: AuthState) => void): AuthStateSubscription;
}
