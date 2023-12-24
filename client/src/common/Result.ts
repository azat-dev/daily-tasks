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

    export const mapError = <Value, OriginalError, MappedError>(
        result: Result<Value, OriginalError>,
        map: (error: OriginalError) => MappedError
    ): Result<Value, MappedError> => {
        switch (result.type) {
            case ResultType.Success:
                return Result.success(result.value);
            case ResultType.Failure:
                return Result.failure(map(result.error));
        }
    };

    export const eraseError = <Value, OriginalError>(
        result: Result<Value, OriginalError>
    ): Result<Value, undefined> => {
        switch (result.type) {
            case ResultType.Success:
                return Result.success(result.value);
            case ResultType.Failure:
                return Result.failure(undefined);
        }
    };
}
