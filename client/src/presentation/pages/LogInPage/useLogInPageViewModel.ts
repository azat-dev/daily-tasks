import { useState } from "react";
import LogInPageViewModel from "./LogInPageViewModel";
import { ILogInByUserNameAndPasswordUseCase } from "../../../domain/usecases/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCase";
import { ResultType } from "../../../common/Result";

const useLogInPageViewModel = (
    logInUseCase: ILogInByUserNameAndPasswordUseCase
): LogInPageViewModel => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isProcessing, setIsProcessing] = useState(false);
    const [showWrongCredentialsErrorText, setShowCredentialsErrorText] =
        useState(false);

    const [highlightAsErrorUserNameInput, setHighlightAsErrorUserNameInput] =
        useState(false);
    const [highlightAsErrorPasswordInput, setHighlightAsErrorPasswordInput] =
        useState(false);

    const resetErrors = () => {
        setShowCredentialsErrorText(false);
        setHighlightAsErrorPasswordInput(false);
        setHighlightAsErrorUserNameInput(false);
    };
    const onSubmit = async (e: any) => {
        e.preventDefault();
        e.stopPropagation();

        setIsProcessing(true);
        resetErrors();

        const cleanedUserName = username.trim().toLowerCase();
        let oneOfInputsEmpty = false;
        if (!cleanedUserName) {
            setHighlightAsErrorUserNameInput(true);
            oneOfInputsEmpty = true;
        }

        if (!password) {
            setHighlightAsErrorPasswordInput(true);
            oneOfInputsEmpty = true;
        }

        if (oneOfInputsEmpty) {
            setIsProcessing(false);
            return;
        }

        try {
            const result = await logInUseCase.logInByUserName(
                cleanedUserName,
                password
            );

            if (result.type === ResultType.Failure) {
                setShowCredentialsErrorText(true);
                setHighlightAsErrorUserNameInput(true);
                setHighlightAsErrorPasswordInput(true);
            }
        } catch (e) {
        } finally {
            setIsProcessing(false);
        }
    };

    const onChangeUserName = (e: any) => {
        resetErrors();
        const newUserName = e.target.value;
        setUsername(newUserName);
    };

    const onChangePassword = (e: any) => {
        resetErrors();
        const newPassword = e.target.value;
        setPassword(newPassword);
    };

    return {
        isProcessing,
        username,
        password,
        onChangeUserName,
        onChangePassword,
        onSubmit,
        highlightAsErrorUserNameInput,
        highlightAsErrorPasswordInput,
        showWrongCredentialsErrorText,
    };
};

export default useLogInPageViewModel;
