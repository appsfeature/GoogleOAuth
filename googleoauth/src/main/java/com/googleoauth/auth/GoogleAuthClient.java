package com.googleoauth.auth;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.googleoauth.listeners.AuthResultCallback;
import com.googleoauth.provider.AuthProvider;
import com.googleoauth.provider.AuthResult;

public class GoogleAuthClient extends AuthClient {

    private static final int GOOGLE_LOGIN_REQUEST = 12121;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;

    private AuthResultCallback<Void> loginCallback;

    @Override
    public void login(@NonNull final Activity activity, @NonNull final AuthResultCallback<Void> callback) {
        googleInit(activity, new AuthResultCallback<Void>() {
            @Override
            public void onResult(AuthResult<Void> googleInitResult) {
                if (googleInitResult.isSuccess()) {
                    final GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(activity);
                    if (lastAccount != null) {
                        googleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                handleGoogleSignInAccount(task, callback);
                            }
                        });

                    } else {
                        loginCallback = callback;
                        Intent googleLoginIntent = googleSignInClient.getSignInIntent();
                        activity.startActivityForResult(googleLoginIntent, GOOGLE_LOGIN_REQUEST);
                    }
                } else {
                    callback.onResult(googleInitResult);
                }
            }
        });
    }

    private void googleInit(final Activity mActivity, AuthResultCallback<Void> callback) {
        if (AuthProvider.getInstance().getServerId() == null) {
            callback.onResult(AuthResult.getFailResult(AUTH_CLIENT_PROVIDER_ERROR, "SERVER_ID_NULL"));
        } else {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(AuthProvider.getInstance().getServerId())
                    .build();

            googleSignInClient = GoogleSignIn.getClient(mActivity, gso);
            if (googleSignInClient == null) {
                callback.onResult(AuthResult.getFailResult(AUTH_CLIENT_INIT_ERROR, "FAIL_TO_INIT_GOOGLE_CLIENT"));
            } else {
                callback.onResult(AuthResult.getSuccessResult());
            }
        }
    }

    @Override
    public void logout() {
        if (googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        clear();
    }

    private void clear() {
        googleSignInClient = null;
        googleSignInAccount = null;
    }

    @Override
    public String getToken() {
        return googleSignInAccount != null ? googleSignInAccount.getIdToken() : "";
    }

    @Override
    public String getEmail() {
        return googleSignInAccount != null ? googleSignInAccount.getEmail() : "";
    }

    @Override
    public boolean isSignedIn(@NonNull Activity activity) {
        return googleSignInAccount != null && (GoogleSignIn.getLastSignedInAccount(activity) != null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_LOGIN_REQUEST) {
            if (loginCallback != null) {
                handleGoogleSignInAccount(GoogleSignIn.getSignedInAccountFromIntent(data), loginCallback);
            }
        }
    }

    private void handleGoogleSignInAccount(Task<GoogleSignInAccount> task, AuthResultCallback<Void> callback) {
        if (callback != null) {
            try {
                googleSignInAccount = task.getResult(ApiException.class);
                callback.onResult(AuthResult.getSuccessResult());
            } catch (ApiException e) {
                googleSignInAccount = null;
                int errorResponse = AUTH_CLIENT_BASE_ERROR - e.getStatusCode();
                String errorMsg = e.toString();
                if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                    errorResponse = AUTH_CLIENT_USER_CANCELLED_ERROR;
                    errorMsg = AUTH_CLIENT_USER_CANCELLED_ERROR_MESSAGE;
                }

                callback.onResult(AuthResult.getFailResult(errorResponse, errorMsg));
            }
        }
    }

}
