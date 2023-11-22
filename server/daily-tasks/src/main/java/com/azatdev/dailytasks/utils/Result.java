package com.azatdev.dailytasks.utils;

public class Result<Value, Failure> {

    private final Value value;
    private final Failure error;
    private final boolean isSuccess;

    private Result(Value value, Failure error, boolean isSuccess) {
        this.value = value;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <Value, Failure> Result<Value, Failure> success(Value value) {
        return new Result<>(value, null, true);
    }

    public static <Value, Failure> Result<Value, Failure> failure(Failure error) {
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Value getValue() throws IllegalStateException {
        if (!isSuccess) {
            throw new IllegalStateException("Result does not contain a value");
        }
        return value;
    }

    public Failure getError() throws IllegalStateException {
        if (isSuccess) {
            throw new IllegalStateException("Result does not contain an error");
        }
        return error;
    }
}