package com.sakhatech.parkme.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Display;


import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bheema.
 */
public class Utility {

    public static final long millisToSeconds(long timeMillis) {
        return timeMillis / 1000;
    }

    public static final long secondsToMillis(long timeSeconds) {
        return timeSeconds * 1000;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Drawable getSupportDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.getResources().getDrawable(resId);
        } else {
            return context.getResources().getDrawable(resId, null);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }





    public static int getDeviceDisplayHeight(Activity registrationActivity) {

        Display display = registrationActivity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 13) {
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            return height;
        } else {
            int height = display.getHeight();  // deprecated
            return height;
        }
    }

    public static String getDeivceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = null;
        if (tm != null) {
            id = tm.getDeviceId();
        }
        if (id == null || isAllZero(id)) {
            id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    private static boolean isAllZero(String id) {
        String allZeroPattern = "^0*$";
        Pattern pattern = Pattern.compile(allZeroPattern);
        Matcher matcher = pattern.matcher(id);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }



    public static String format(double value) {
        if (value < 1000) {
            return format("###.##", value);
        } else {
            double hundreds = value % 1000;
            int other = (int) (value / 1000);
            return format(",##", other) + ',' + format("000.##", hundreds);
        }
    }

    public static String getFormatedAmount(double value) {
        String amount = format(value);
        if (amount.contains(".")) {
            String decimalDigit = amount.substring(amount.lastIndexOf(".") + 1, amount.length());
            if (decimalDigit.length() == 1) {
                return amount + "0";
            } else if (decimalDigit.length() == 0) {
                return amount + ".00";
            }
        } else if (amount.equalsIgnoreCase("0")) {
            return "0.00";

        } else {
            return amount + ".00";
        }
        return amount;
    }

    public static String getAmountWithDollar(double amount){
        String fAmount=getFormatedAmount(amount);
        return "\u0024"+fAmount;
    }

    private static String format(String pattern, Object value) {
        return new DecimalFormat(pattern).format(value);
    }

    public static int getDpToPixels(Context context, int dp) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }



    public static String getDate(long createdAt, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(createdAt);
    }

    public static String getTime(long milliseconds)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
        return simpleDateFormat.format(milliseconds);
    }

    public static String encodeToBase64(String data) {
        byte[] encodedBytes = Base64.encode(data.getBytes(), Base64.NO_WRAP);
        return new String(encodedBytes);
    }

    public static String decodeBase64(String data) {
        byte[] decodedBytes = Base64.decode(data.getBytes(), Base64.NO_WRAP);
        return new String(decodedBytes);
    }

    public static String formatString(String json) {
        json = json.replaceAll(" ", "");
        json = json.replaceAll("\n", "");
        return json;
    }





    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }


    public static File createTempFile(Context context, String fileName) {
        File mFileTemp;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), fileName);
        } else {
            mFileTemp = new File(context.getFilesDir(), fileName);
        }
        return mFileTemp;
    }

    public static File getProfileFile(Context context, String filename) {
        File mFileTemp;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), filename);
        } else {
            mFileTemp = new File(context.getFilesDir(), filename);
        }
        return mFileTemp;
    }


    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }


    public static String getSpentDuration(long timeInMillis) {
        long temp_long = timeInMillis / 1000;

        long second = temp_long % 60;
        long hour = temp_long / 3600;
        long minute = (temp_long / 60) % 60;
        String tempTimer;
        tempTimer = ((hour < 10) ? "0" + hour : "" + hour) + ((minute < 10) ? ":0" + minute : ":" + minute) + ((second < 10) ? ":0" + second : ":" + second);
        return tempTimer;
    }


    public static Bitmap writeTextOnDrawable(int drawableId, String text,Context context) {

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(context, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 10;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) -10) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }




}
