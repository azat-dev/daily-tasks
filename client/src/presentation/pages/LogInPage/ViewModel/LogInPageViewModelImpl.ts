import { useMemo } from "react";
import LogInPageViewModel from "./LogInPageViewModel";
import { LogInByUserNameAndPasswordUseCase } from "../../../../domain/usecases/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCase";
import { ResultType } from "../../../../common/Result";

import Subject from "../../../utils/Subject";
import value from "../../../utils/value";

export default class LogInPageViewModelImpl implements LogInPageViewModel {
    // Properties

    public username: Subject<string> = value("");
    public password: Subject<string> = value("");
    public isProcessing: Subject<boolean> = value(false);
    public showWrongCredentialsErrorText: Subject<boolean> = value(false);
    public highlightAsErrorUserNameInput: Subject<boolean> = value(false);
    public highlightAsErrorPasswordInput: Subject<boolean> = value(false);

    // Constructors

    public constructor(
        private logInUseCase: LogInByUserNameAndPasswordUseCase
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
