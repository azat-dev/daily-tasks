import ISubject from "../../../utils/ISubject";

export interface LogInPageViewModelOutput {
    isProcessing: ISubject<boolean>;
    username: ISubject<string>;
    password: ISubject<string>;
    highlightAsErrorUserNameInput: ISubject<boolean>;
    highlightAsErrorPasswordInput: ISubject<boolean>;
    showWrongCredentialsErrorText: ISubject<boolean>;
}

export interface LogInPageViewModelInput {
    onChangeUserName: (e: any) => void;
    onChangePassword: (e: any) => void;
    onSubmit: (e: any) => Promise<void>;
}

export default interface LogInPageViewModel
    extends LogInPageViewModelOutput,
        LogInPageViewModelInput {}
