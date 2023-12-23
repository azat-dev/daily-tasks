import React from "react";
import { Form, Button, FloatingLabel, Spinner } from "react-bootstrap";

import logo from "../../../logo.svg";
import styles from "./styles.module.scss";
import LogInPageViewModel from "./ViewModel/LogInPageViewModel";
import useUpdatesFrom from "../../utils/useUpdatesFrom";

export interface LogInPageViewProps {
    viewModel: LogInPageViewModel;
}

const LogInPageView = ({ viewModel: vm }: LogInPageViewProps) => {
    useUpdatesFrom(
        vm.username,
        vm.password,
        vm.isProcessing,
        vm.highlightAsErrorPasswordInput,
        vm.highlightAsErrorUserNameInput,
        vm.showWrongCredentialsErrorText
    );
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

                    <FloatingLabel label="User name">
                        <Form.Control
                            type="text"
                            placeholder="Username..."
                            autoComplete="username"
                            autoFocus={true}
                            id="username"
                            autoCapitalize="none"
                            spellCheck={false}
                            value={vm.username.value}
                            disabled={vm.isProcessing.value}
                            onChange={vm.onChangeUserName}
                            isInvalid={vm.highlightAsErrorUserNameInput.value}
                        />
                    </FloatingLabel>
                    <FloatingLabel label="Password...">
                        <Form.Control
                            name="password"
                            type="password"
                            autoComplete="current-password"
                            autoCapitalize="none"
                            spellCheck={false}
                            id="password"
                            value={vm.password.value}
                            placeholder="Password"
                            disabled={vm.isProcessing.value}
                            onChange={vm.onChangePassword}
                            isInvalid={vm.highlightAsErrorPasswordInput.value}
                        />
                    </FloatingLabel>

                    <Form.Group className="mb-3">
                        <Form.Check
                            id="rememberMeCheckBox"
                            type="checkbox"
                            label="Remember me"
                            style={{ display: "none" }}
                            disabled={vm.isProcessing.value}
                        />
                    </Form.Group>

                    <Button
                        variant="primary"
                        type="submit"
                        className="w-100 btn btn-lg btn-primary"
                        disabled={vm.isProcessing.value}
                        onClick={vm.onSubmit}
                    >
                        <Spinner
                            as="span"
                            animation="border"
                            size="sm"
                            role="status"
                            aria-hidden="true"
                            hidden={!vm.isProcessing.value}
                        />
                        <span hidden={vm.isProcessing.value}>Sign in</span>
                    </Button>

                    {vm.showWrongCredentialsErrorText.value && (
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
