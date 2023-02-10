# Google OAuth
Using OAuth 2.0 to Sign in Google | Authorization | Access Google APIs

Google APIs use the OAuth 2.0 protocol for authentication and authorization. 
Google supports common OAuth 2.0 scenarios such as those for web server, client-side, 
installed, and limited-input device applications.
A library designed to use Oauth 2.0 easily

OAuth Login! 

Super simple, super easy to use!

## Quick Setup

### 1. Include library

**Using Gradle**

Step 1. Add the JitPack repository to your build file

``` gradle
allprojects {
    repositories {
    	...
        maven { url 'https://jitpack.io' }
        
    }
}
```

Step 2. Add the dependency

``` gradle
dependencies {
    implementation 'com.github.appsfeature:GoogleOAuth:1.0'
}
```

### 2. Usage

* In Activity :

#### a. Set Server Id

set Google OAuth 2.0 'Web Client ID'

``` java
    GoogleOAuthSession.setAuthProvider("<OAuth 2.0 Web Client ID>");
```

#### b. Override onActivityResult

``` java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleOAuthSession.onActivityResult(requestCode,resultCode,data);
    }
```

#### C. Call Login API

``` java 
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
```

### 3. Getters

``` java
    //Get Sign in Status
    GoogleOAuthSession.isSignedIn(<Activity>);
    
    //Get Access Token
    GoogleOAuthSession.getAccessToken();
    
    //Get Email
    GoogleOAuthSession.getEmail();
    
    //Logout
    GoogleOAuthSession.logout();
```

