import { error } from "console";

export enum ResultType {
    Success = "Success",
    Failure = "Failure",
}

export type Result<SuccessValue, ErrorValue> =
    | { type: ResultType.Success; value: SuccessValue }
    | { type: ResultType.Failure; error: ErrorValue };

export namespace Result {
    const failure = <T>(error: T) => ({ type: ResultType.Failure, error });
    const success = <T>(value: T) => ({ type: ResultType.Success, value });
}
