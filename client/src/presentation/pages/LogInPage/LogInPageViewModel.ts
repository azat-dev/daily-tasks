export default interface LogInPageViewModel {
    isProcessing: boolean;
    username: string;
    password: string;
    onChangeUserName: (e: any) => void;
    onChangePassword: (e: any) => void;
    onSubmit: (e: any) => Promise<void>;
    highlightAsErrorUserNameInput: boolean;
    highlightAsErrorPasswordInput: boolean;
    showWrongCredentialsErrorText: boolean;
}
