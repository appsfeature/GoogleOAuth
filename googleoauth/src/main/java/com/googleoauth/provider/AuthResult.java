package com.googleoauth.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AuthResult<T> {
    private final boolean isSuccess;
    private T contents;

    private String errorMessage;
    private int errorCode;

    public AuthResult(boolean isSuccess, T contents) {
        this.isSuccess = isSuccess;
        this.contents = contents;
    }

    public AuthResult(int errorCode, @NonNull String errorMessage) {
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static <T> AuthResult<T> getSuccessResult() {
        return new AuthResult<>(true, null);
    }

    public static <T> AuthResult<T> getFailResult(int errorCode, @NonNull String errorMessaage) {
        return new AuthResult<>(errorCode, errorMessaage);
    }

    public T getContents() {
        return contents;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
