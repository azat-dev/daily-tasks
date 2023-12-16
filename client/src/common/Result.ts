import { error } from "console";

export enum ResultType {
    Success = "Success",
    Failure = "Failure",
}

export type Result<SuccessValue, ErrorValue> =
    | { type: ResultType.Success; value: SuccessValue }
    | { type: ResultType.Failure; error: ErrorValue };

export namespace Result {
    export const failure = <T>(error: T): Result<any, T> => ({
        type: ResultType.Failure,
        error,
    });
    export const success = <T>(value: T): Result<T, any> => ({
        type: ResultType.Success,
        value,
    });
}
