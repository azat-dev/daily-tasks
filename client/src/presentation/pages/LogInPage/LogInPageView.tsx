import React from "react";
import { Form, Button, FloatingLabel, Spinner } from "react-bootstrap";

import logo from "../../../logo.svg";
import styles from "./styles.module.scss";
import LogInPageViewModel from "./LogInPageViewModel";

export interface LogInPageViewProps {
    viewModel: LogInPageViewModel;
}

const LogInPageView = ({ viewModel }: LogInPageViewProps) => {
    return (
        <div className={styles.signInPage}>
            <main className={`${styles.formLogIn} w-100 m-auto`}>
                <Form>
                    <img
                        className="mb-4"
                        src={logo}
                        alt=""
                        width="72"
                        height="57"
                    />
                    <h1 className="h3 mb-3 fw-normal">Please sign in</h1>

                    <FloatingLabel controlId="usernameInput" label="User name">
                        <Form.Control
                            type="text"
                            placeholder="Username..."
                            autoComplete="username"
                            autoFocus={true}
                            id="username"
                            autoCapitalize="none"
                            spellCheck={false}
                            value={viewModel.username}
                            disabled={viewModel.isProcessing}
                            onChange={viewModel.onChangeUserName}
                            isInvalid={viewModel.highlightAsErrorUserNameInput}
                        />
                    </FloatingLabel>
                    <FloatingLabel
                        controlId="floatingPassword"
                        label="Password..."
                    >
                        <Form.Control
                            name="password"
                            type="password"
                            autoComplete="current-password"
                            autoCapitalize="none"
                            spellCheck={false}
                            id="password"
                            value={viewModel.password}
                            placeholder="Password"
                            disabled={viewModel.isProcessing}
                            onChange={viewModel.onChangePassword}
                            isInvalid={viewModel.highlightAsErrorPasswordInput}
                        />
                    </FloatingLabel>

                    <Form.Group className="mb-3" controlId="formBasicCheckbox">
                        <Form.Check
                            type="checkbox"
                            label="Remember me"
                            style={{ display: "none" }}
                            disabled={viewModel.isProcessing}
                        />
                    </Form.Group>

                    <Button
                        variant="primary"
                        type="submit"
                        className="w-100 btn btn-lg btn-primary"
                        disabled={viewModel.isProcessing}
                        onClick={viewModel.onSubmit}
                    >
                        <Spinner
                            as="span"
                            animation="border"
                            size="sm"
                            role="status"
                            aria-hidden="true"
                            hidden={!viewModel.isProcessing}
                        />
                        <span hidden={viewModel.isProcessing}>Sign in</span>
                    </Button>

                    {viewModel.showWrongCredentialsErrorText && (
                        <div className="invalid-feedback">
                            You must agree before submitting.
                        </div>
                    )}
                </Form>
            </main>
        </div>
    );
};

export default React.memo(LogInPageView);
