package com.sakhatech.parkme.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sakhatech.spotizen.R;
import com.sakhatech.parkme.util.Constants;
import com.sakhatech.parkme.util.PermissionChecker;


/**
 * Created by Bheema.
 */
public class BaseActivity extends AppCompatActivity implements PermissionChecker.PermissionActionListener {

    protected Toolbar mToolbar;
    TextView mTitle, mSubTitle;
    RelativeLayout mAddWalletLayout;
    ImageView mAddFilterIcon;
    protected static final int MY_PERMISSIONS_LOCATION = 1;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


    AlertDialog alertDialog;

    public void showDialog(DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelListener, String okText, String cancelText, String title, String message, boolean isCancelOnOutsideTouch, boolean isLaunchPlaystore) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message);
        if (!isCancelOnOutsideTouch) {
            alertDialogBuilder.setCancelable(false);
        } else {
            alertDialogBuilder.setCancelable(true);
        }
        alertDialogBuilder.setPositiveButton(okText, okClickListener)
                .setNegativeButton(cancelText, cancelListener);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        if (isLaunchPlaystore) {

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO launch playstore
//                            Utility.launchPlayStore(BaseActivity.this);
                        }
                    });
                }
            });
        }

        // show it
        if (!isFinishing()) {
            alertDialog.show();
        }
    }


    public void showDialog(DialogInterface.OnClickListener okClickListener, String okText, String title, String message, boolean isCancelOnOutsideTouch) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message);
        if (!isCancelOnOutsideTouch) {
            alertDialogBuilder.setCancelable(false);
        } else {
            alertDialogBuilder.setCancelable(true);
        }
        if (okClickListener == null) {
            okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissDailog();
                }
            };
        }
        alertDialogBuilder.setPositiveButton(okText, okClickListener);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void dismissDailog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void showToast(String message, int length) {
        if (!isFinishing()) {
            Toast toast = Toast.makeText(this, message, length);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
            toast.show();
        }
    }


    protected void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }

    protected void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    public void sendGCMRequest() {
//        if (checkPlayServices()) {
////            ParkMeApplication.getInstance().initializeGcmRegistration();
//        }
//    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
//    private boolean checkPlayServices() {
//
//        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = this.getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
////            window.setStatusBarColor(getResources().getColor(R.color.status_color));
//
//            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_color));
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void noSufficientPermission(final int requestCode) {
        switch (requestCode) {

            case Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE:
                showDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onPermissionActionTaken(requestCode, true);

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPermissionActionTaken(requestCode, false);

                    }
                }, getString(R.string.button_ok), getString(R.string.cancel), getString(R.string.permission_required), getString(R.string.read_phone_state_permission_not_available), false, false);

                break;
            case Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE:
                showDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        onPermissionActionTaken(requestCode, true);

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onPermissionActionTaken(requestCode, false);

                    }
                }, getString(R.string.button_ok), getString(R.string.cancel), getString(R.string.permission_required), getString(R.string.location_permission_not_available), false, false);

                break;
        }


    }

    public void onPermissionActionTaken(int requestCode, boolean isLaunch) {
        switch (requestCode) {

            case Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE:
                if (isLaunch) {
                    launchSettingAppWithResult(BaseActivity.this, Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE);

                } else {
                    finish();
                }

                break;
            case Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE:
                if (isLaunch) {
                    launchSettingAppWithResult(BaseActivity.this, Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE);

                } else {
                    finish();
                }

                break;

        }
    }

    public static void launchSettingAppWithResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((BaseActivity) context).startActivityForResult(intent, requestCode);

    }

    public void hideToolBar() {
        getSupportActionBar().hide();
    }

    public void showToolBar() {
        getSupportActionBar().show();
    }
}
