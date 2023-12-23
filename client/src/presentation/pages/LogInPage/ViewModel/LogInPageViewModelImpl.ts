import { useMemo } from "react";
import LogInPageViewModel from "./LogInPageViewModel";
import { ILogInByUserNameAndPasswordUseCase } from "../../../../domain/usecases/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCase";
import { ResultType } from "../../../../common/Result";

import ISubject from "../../../utils/ISubject";
import { value } from "../../../utils/Subject";

export default class LogInPageViewModelImpl implements LogInPageViewModel {
    // Properties

    public username: ISubject<string> = value("");
    public password: ISubject<string> = value("");
    public isProcessing: ISubject<boolean> = value(false);
    public showWrongCredentialsErrorText: ISubject<boolean> = value(false);
    public highlightAsErrorUserNameInput: ISubject<boolean> = value(false);
    public highlightAsErrorPasswordInput: ISubject<boolean> = value(false);

    // Constructors

    public constructor(
        private logInUseCase: ILogInByUserNameAndPasswordUseCase
    ) {}

    // Methods

    public resetErrors = () => {
        this.showWrongCredentialsErrorText.set(false);
        this.highlightAsErrorUserNameInput.set(false);
        this.highlightAsErrorPasswordInput.set(false);
    };

    public onSubmit = async (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        this.isProcessing.set(true);
        this.resetErrors();

        const cleanedUserName = this.username.value.trim().toLowerCase();
        let oneOfInputsEmpty = false;
        if (!cleanedUserName) {
            this.highlightAsErrorUserNameInput.set(true);
            oneOfInputsEmpty = true;
        }

        if (!this.password.value) {
            this.highlightAsErrorPasswordInput.set(true);
            oneOfInputsEmpty = true;
        }

        if (oneOfInputsEmpty) {
            this.isProcessing.set(false);
            return;
        }

        try {
            const result = await this.logInUseCase.logInByUserName(
                cleanedUserName,
                this.password.value
            );

            if (result.type === ResultType.Failure) {
                this.showWrongCredentialsErrorText.set(true);
                this.highlightAsErrorPasswordInput.set(true);
                this.highlightAsErrorUserNameInput.set(true);
            }
        } catch (e) {
        } finally {
            this.isProcessing.set(false);
        }
    };

    public onChangeUserName = (e: any) => {
        this.resetErrors();
        const newUserName = e.target.value;
        this.username.set(newUserName);
    };

    public onChangePassword = (e: any) => {
        this.resetErrors();
        const newPassword = e.target.value;
        this.password.set(newPassword);
    };
}
