import React from "react";

import {
    createBrowserRouter,
    Navigate,
    RouterProvider,
} from "react-router-dom";
import AuthProcessingPage from "../pages/AuthProcessingPage";
import { RequireAuth } from "./require-auth";
import AuthState from "../../domain/models/auth/AuthState";
import PageWithSidebarView from "../components/Sidebar/PageWithSidebar/PageWithSidebarView";

export interface Pages {
    SignInPage: React.ComponentType;
    DayPage: React.ComponentType;
    WeekPage: React.ComponentType;
}

interface AppRouterProps {
    authState: AuthState;
    signInPagePath: string;
    pages: Pages;
}

const AppRouter = ({ authState, signInPagePath, pages }: AppRouterProps) => {
    const routes = [
        {
            path: signInPagePath,
            Component: () => {
                if (authState === AuthState.LOGGED_IN) {
                    return <Navigate replace to="/" />;
                }

                return <pages.SignInPage />;
            },
        },
        {
            path: "/",
            exact: true,
            element: (
                <RequireAuth.Component
                    authState={authState}
                    redirectTo={signInPagePath}
                    AuthProcessingComponent={AuthProcessingPage}
                    Component={() => <Navigate replace to="/backlog/day" />}
                />
            ),
        },
        {
            path: "/backlog/day",
            element: (
                <RequireAuth.Component
                    authState={authState}
                    redirectTo={signInPagePath}
                    AuthProcessingComponent={AuthProcessingPage}
                    Component={() => {
                        return (
                            <PageWithSidebarView activeItemId="day">
                                <pages.DayPage />
                            </PageWithSidebarView>
                        );
                    }}
                />
            ),
        },
    ];

    const router = createBrowserRouter(routes);
    return <RouterProvider router={router} />;
};

export default React.memo(AppRouter);
