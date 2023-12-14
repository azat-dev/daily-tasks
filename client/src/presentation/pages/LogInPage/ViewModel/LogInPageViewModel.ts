import Value from "../Value";

export interface LogInPageViewModelOutput {
    isProcessing: Value<boolean>;
    username: Value<string>;
    password: Value<string>;
    highlightAsErrorUserNameInput: Value<boolean>;
    highlightAsErrorPasswordInput: Value<boolean>;
    showWrongCredentialsErrorText: Value<boolean>;
}

export interface LogInPageViewModelInput {
    onChangeUserName: (e: any) => void;
    onChangePassword: (e: any) => void;
    onSubmit: (e: any) => Promise<void>;
}

export default interface LogInPageViewModel
    extends LogInPageViewModelOutput,
        LogInPageViewModelInput {}
