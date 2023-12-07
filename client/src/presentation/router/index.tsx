import React from "react";

import {
    createBrowserRouter,
    Navigate,
    RouterProvider,
    useParams,
} from "react-router-dom";
import AuthProcessingPage from "../pages/AuthProcessingPage";
import { RequireAuth } from "./require-auth";
import AuthState from "../../domain/models/AuthState";
import PageWithSidebarView from "../components/Sidebar/PageWithSidebar/PageWithSidebarView";

interface AppRouterProps {
    authState: AuthState;
    signInPagePath: string;
    views: {
        SignInPage: React.ComponentType;
        BacklogPage: React.ComponentType;
        DayPage: React.ComponentType;
        WeekPage: React.ComponentType;
    };
}

const BacklogPageWithSidebar: React.FC = () => {
    const params = useParams();
    return (
        <PageWithSidebarView
            activeItemId={params.period?.toLowerCase() ?? ("day" as any)}
        />
    );
};

const AppRouter = ({ authState, signInPagePath, views }: AppRouterProps) => {
    const BacklogContent = () => {
        const params = useParams();

        switch (params.backlogType) {
            case "day":
                return <views.DayPage />;
            default:
                return null;
        }
    };

    const routes = [
        {
            path: signInPagePath,
            Component: () => {
                if (authState === AuthState.LOGGED_IN) {
                    return <Navigate replace to="/" />;
                }

                return <views.SignInPage />;
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
                    Component={() => (
                        <Navigate replace to="/tasks/backlog/day" />
                    )}
                />
            ),
        },
        {
            path: "/",
            element: (
                <RequireAuth.Component
                    authState={authState}
                    redirectTo={signInPagePath}
                    AuthProcessingComponent={AuthProcessingPage}
                    Component={BacklogPageWithSidebar}
                />
            ),
            children: [
                {
                    path: "tasks/backlog",
                    element: (
                        <RequireAuth.Component
                            authState={authState}
                            redirectTo={signInPagePath}
                            AuthProcessingComponent={AuthProcessingPage}
                            Component={views.BacklogPage}
                        />
                    ),
                    children: [
                        {
                            path: ":backlogType",
                            element: (
                                <RequireAuth.Component
                                    authState={authState}
                                    redirectTo={signInPagePath}
                                    AuthProcessingComponent={AuthProcessingPage}
                                    Component={BacklogContent}
                                />
                            ),
                        },
                    ],
                },
            ],
        },
    ];

    const router = createBrowserRouter(routes);
    return <RouterProvider router={router} />;
};

export default React.memo(AppRouter);
