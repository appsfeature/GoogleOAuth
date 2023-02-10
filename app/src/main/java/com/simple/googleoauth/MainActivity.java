package com.simple.googleoauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.googleoauth.GoogleOAuthSession;
import com.googleoauth.listeners.AuthResultCallback;
import com.googleoauth.provider.AuthResult;
import com.sample.googleoauth.R;

public class MainActivity extends AppCompatActivity {

    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tv_status);

        GoogleOAuthSession.setAuthProvider("532271504370-irf3thlpul1n5cpvqnr9868g3eo5790k.apps.googleusercontent.com");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleOAuthSession.onActivityResult(requestCode,resultCode,data);
    }

    public void onGoogleLoginClick(View v){
        GoogleOAuthSession.login(this, new AuthResultCallback<Void>() {
            @Override
            public void onResult(AuthResult<Void> result) {
                if(result.isSuccess()){
                    //Success
                    tvStatus.setText("Login is succeed");
                }else{
                    //Fail to Login!
                    int errorCode = result.getErrorCode();
                    String errorMessage = result.getErrorMessage();
                    tvStatus.setText("FAIL / " + errorCode + " / " + errorMessage);
                }
            }
        });
    }

    public void onLogoutClick(View v){
        GoogleOAuthSession.logout();
        tvStatus.setText("Logout!");
    }

    public void onLoggedInClick(View v){
        tvStatus.setText((GoogleOAuthSession.isSignedIn(this) ? "Yes!" : "No.."));
    }

    public void onGetAccessTokenClick(View v){
        tvStatus.setText(GoogleOAuthSession.getAccessToken());
    }

    public void onGetEmailClick(View v){
        tvStatus.setText(GoogleOAuthSession.getEmail());
    }


}
