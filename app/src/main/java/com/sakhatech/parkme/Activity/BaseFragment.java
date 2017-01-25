package com.sakhatech.parkme.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sakhatech.parkme.util.Constants;

/**
 * Created by bheema.
 */
public class BaseFragment extends Fragment {
    AlertDialog alertDialog;

    public void showDialog(final DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelListener, String okText, String cancelText, String title, String message, boolean isCancelOnOutsideTouch, boolean isLaunchPlaystore) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

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
                            //TODO add launch playstore
//                            Utility.launchPlayStore(getActivity());
                        }
                    });
                }
            });
        }
        // show it
        alertDialog.show();
    }


    public void showDialog(DialogInterface.OnClickListener okClickListener, String okText, String title, String message, boolean isCancelOnOutsideTouch) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

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
                    alertDialog.dismiss();
                }
            };
        }
        alertDialogBuilder.setPositiveButton(okText, okClickListener);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void showToast(String message, int length) {
        if (!getActivity().isFinishing()) {
            Toast toast = Toast.makeText(getActivity(), message, length);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);

            toast.show();

        }
    }

    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void onPermissionActionTaken(int requestCode, boolean isLaunch) {
        switch (requestCode) {

            case Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE:
                if (isLaunch) {
                    launchSettingAppWithResult(Constants.READ_PHONE_STATUS_PERMISSION_REQUEST_CODE);

                } else {
                    getActivity().finish();
                }

                break;
            case Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE:
                if (isLaunch) {
                    launchSettingAppWithResult(Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE);

                } else {
                    getActivity().finish();
                }

                break;

        }
    }

    public void launchSettingAppWithResult(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        BaseFragment.this.getActivity().startActivityForResult(intent, requestCode);

    }
}
