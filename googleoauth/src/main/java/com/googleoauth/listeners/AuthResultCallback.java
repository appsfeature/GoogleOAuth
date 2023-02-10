package com.googleoauth.listeners;

import com.googleoauth.provider.AuthResult;

public interface AuthResultCallback<T> {
    void onResult(AuthResult<T> result);
}

