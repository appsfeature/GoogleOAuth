package com.googleoauth;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.googleoauth.auth.AuthClient;
import com.googleoauth.listeners.AuthResultCallback;
import com.googleoauth.provider.AuthResult;
import com.googleoauth.provider.AuthProvider;

public class GoogleOAuthSession {

    private static AuthClient currentClient;

    public GoogleOAuthSession() {
        currentClient = null;
    }

    public static void setAuthProvider(@NonNull String serverId) {
        AuthProvider.getInstance().addServerId(serverId);
    }

    public static void login(@NonNull Activity activity, @NonNull final AuthResultCallback<Void> callback) {
        final AuthClient authClient = AuthClient.getAuthClientInstance();
        GoogleOAuthSession.currentClient = authClient;
        authClient.login(activity, new AuthResultCallback<Void>() {
            @Override
            public void onResult(AuthResult<Void> result) {

                if (!result.isSuccess()) {
                    GoogleOAuthSession.currentClient = null;
                }

                callback.onResult(result);
            }
        });
    }

    public static void logout() {
        if (currentClient != null) {
            currentClient.logout();

            currentClient = null;
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (currentClient != null) {
            currentClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static boolean isSignedIn(@NonNull Activity activity) {
        if (currentClient != null) {
            return currentClient.isSignedIn(activity);
        }
        return false;
    }

    public static String getAccessToken() {
        if (currentClient != null)
            return currentClient.getToken();
        return "";
    }

    public static String getEmail() {
        if (currentClient != null)
            return currentClient.getEmail();
        return "";
    }

}
