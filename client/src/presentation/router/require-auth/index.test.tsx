import React from "react";
import { render, waitFor, screen } from "@testing-library/react";
import { RouterProvider, createMemoryRouter } from "react-router-dom";
import { RequireAuth } from "./index";
import AuthState from "../../../domain/models/AuthState";

type SutTypes = {
    location: any;
};

const makeSut = (authState: AuthState): SutTypes => {
    const result: any = { location: null };

    const router = createMemoryRouter(
        [
            {
                path: "/login",
                element: <div data-testid="login-view">Login</div>,
            },
            {
                path: "/",
                element: (
                    <RequireAuth.Component
                        authState={authState}
                        redirectTo="/login"
                        Component={() => {
                            return (
                                <div data-testid="private-view" role="main">
                                    Private Route
                                </div>
                            );
                        }}
                        AuthProcessingComponent={() => (
                            <div id="processing-view" role="main">
                                Processing
                            </div>
                        )}
                    />
                ),
            },
        ],
        { initialEntries: ["/"] }
    );
    render(<RouterProvider router={router} />);

    return result;
};

describe("PrivateRoute", () => {
    test("Should redirect to /login if token is empty", async () => {
        // Given
        makeSut(AuthState.LOGGED_OUT);

        // When
        const loginView = await waitFor(() => screen.getByTestId("login-view"));

        // Then
        expect(loginView).toBeTruthy();
    });

    test("Should show Component if logged in", async () => {
        // Given
        makeSut(AuthState.LOGGED_IN);

        // When
        const privateView = await waitFor(() =>
            screen.getByTestId("private-view")
        );

        // Then
        expect(privateView).toBeTruthy();
    });
});
