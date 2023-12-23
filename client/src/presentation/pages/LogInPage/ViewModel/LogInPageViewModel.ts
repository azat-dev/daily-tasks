import Subject from "../../../utils/Subject";

export interface LogInPageViewModelOutput {
    isProcessing: Subject<boolean>;
    username: Subject<string>;
    password: Subject<string>;
    highlightAsErrorUserNameInput: Subject<boolean>;
    highlightAsErrorPasswordInput: Subject<boolean>;
    showWrongCredentialsErrorText: Subject<boolean>;
}

export interface LogInPageViewModelInput {
    onChangeUserName: (e: any) => void;
    onChangePassword: (e: any) => void;
    onSubmit: (e: any) => Promise<void>;
}

export default interface LogInPageViewModel
    extends LogInPageViewModelOutput,
        LogInPageViewModelInput {}
