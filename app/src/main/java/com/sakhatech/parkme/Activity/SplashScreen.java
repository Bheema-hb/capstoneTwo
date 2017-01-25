package com.sakhatech.parkme.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.sakhatech.parkme.Activity.signup.LoginScreen;
import com.sakhatech.parkme.Activity.home.HomeActivity;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.spotizen.R;
import com.sakhatech.parkme.util.Constants;
import com.sakhatech.parkme.util.PermissionChecker;

public class SplashScreen extends BaseActivity {
    Handler mHandler;


    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {


                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    startNextScreen();

                } else {
                    if (PermissionChecker.checkIfPermissionGranted(SplashScreen.this, Manifest.permission.READ_PHONE_STATE, Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE, true, SplashScreen.this)) {
                        startNextScreen();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mHandler = new Handler();
        mHandler.postDelayed(mHideRunnable, 1000);

    }

    private void startNextScreen() {
        if (ParkMeSharedPreference.getInstance(SplashScreen.this).isLoggedIn()) {
            //TODO TAKE USER TO DASHBOARD
            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            //User is not loged in take hime to login screen
            Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE:

                switch (resultCode) {
                    case Activity.RESULT_OK:

                        mHandler.postDelayed(mHideRunnable, 0);
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {

                case Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE:
                    startNextScreen();
                    break;
            }

        } else {
            if (requestCode == Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE) {
                finish();
            }
        }


    }

}
