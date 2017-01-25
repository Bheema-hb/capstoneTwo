package com.sakhatech.parkme.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Bheema.
 * // Copyright (c) 2016 Techjini Solutions. All rights reserved.
 */
public class PermissionChecker {

    public static final int GRANT = 1;
    private static final int CANCEL = 2;

    public interface PermissionActionListener {

        public void noSufficientPermission(int requestCode);

    }

    public static boolean checkIfPermissionGranted(Context context, String permission, int requestCode, boolean requestUser, PermissionActionListener permissionActionListener) {
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (requestUser) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    permissionActionListener.noSufficientPermission(requestCode);
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{permission},
                            requestCode);

                }
            }
            return false;

        }
        return true;
    }

}
