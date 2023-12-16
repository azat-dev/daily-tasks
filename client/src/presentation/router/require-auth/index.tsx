import React from "react";
import { RouteProps, Navigate } from "react-router-dom";
import AuthState from "../../../domain/models/AuthState";

export namespace RequireAuth {
    export type Props = {
        authState: AuthState;
        redirectTo: string;
        Component: React.ComponentType;
        AuthProcessingComponent: React.ComponentType;
    } & RouteProps;

    export const Component: React.FC<RequireAuth.Props> = (props) => {
        const { authState, redirectTo, AuthProcessingComponent, Component } =
            props;
        switch (authState) {
            case AuthState.LOGGED_IN:
                return <Component />;

            case AuthState.LOGGED_OUT:
                return <Navigate replace to={redirectTo} />;

            case AuthState.PROCESSING:
                return <AuthProcessingComponent />;
        }
    };
}
