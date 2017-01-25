package com.sakhatech.parkme.Activity.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sakhatech.parkme.Activity.BaseActivity;
import com.sakhatech.parkme.Activity.home.HomeActivity;
import com.sakhatech.spotizen.R;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;

import java.util.Arrays;

/**
 * Created by Bheema
 * Company Techjini
 */
public class LoginScreen extends BaseActivity {
    EditText mEmailEdit, mPassword;
    Button mSignInButton, mCreateAccount, mFacebookLogin;
    TextView mForgotPassword;
    RelativeLayout mLoadingLayout;
    TextView mLoadingText;
    CoordinatorLayout mRootLayout;

    private LoginButton fbButton;
    CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbButton = new LoginButton(this);
        disableFbLoginState();
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_layout);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.login_root_layout);
        mEmailEdit = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.sign_in_id);
        mCreateAccount = (Button) findViewById(R.id.create_account);
        mForgotPassword = (TextView) findViewById(R.id.forgot_password);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        mLoadingText = (TextView) findViewById(R.id.loading_text);
        mFacebookLogin = (Button) findViewById(R.id.sign_up_with_facebook);
        fbButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        fbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //TODO take user to dashboard screen
                ParkMeSharedPreference.getInstance(LoginScreen.this).setFbLogin(true);
                startDashBoardScreen();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserLoginInput();


            }
        });
        mFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbButton.performClick();
            }
        });
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO server sign up
                Intent intent = new Intent(LoginScreen.this, SignUpScreen.class);
                startActivity(intent);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.enter_any_username_password), Toast.LENGTH_LONG);

            }
        });


    }

    private void disableFbLoginState() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager loginManager = LoginManager.getInstance();
            if (loginManager != null) {
                loginManager.logOut();
                ParkMeSharedPreference.getInstance(LoginScreen.this).setFbLogin(false);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void validateUserLoginInput() {

        if (TextUtils.isEmpty(mEmailEdit.getText().toString().trim()) || !Utility.isValidEmail(mEmailEdit.getText().toString().trim())) {
            mEmailEdit.setError(getString(R.string.enter_valid_email));
            mEmailEdit.requestFocus();
            return;
        }
        if (mPassword.getText().toString().trim().length() == 0) {
            mPassword.setError(getString(R.string.enter_password));
            mPassword.requestFocus();
            return;
        }
        sendLoginRequest();

    }

    private void sendLoginRequest() {
        if (Utility.isOnline(this)) {

            ParkMeSharedPreference.getInstance(LoginScreen.this).setIsLoggedIn(true);
            startDashBoardScreen();

        } else {
            showDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissDailog();
                }
            }, getString(R.string.ok), getString(R.string.network_issue), getString(R.string.please_check_your_internet), false);
        }
    }

    private void startDashBoardScreen() {
        Intent intent = new Intent(LoginScreen.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
