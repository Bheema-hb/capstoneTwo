package com.sakhatech.parkme.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ParkMeSharedPreference {


    public static final String FB_LOGGED_IN = "FbLoggedIn";
    public static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String EMAIL = "Email";
    private static final String VEHICLE = "Vehicle";
    private static final String USER_NAME = "UserName";

    private static ParkMeSharedPreference instance;


    private final Context mContext;
    SharedPreferences sharedpreferences;
    private String mParkMePreference = "ParkMePreference";


    private ParkMeSharedPreference(Context context) {
        mContext = context;
        sharedpreferences = mContext.getSharedPreferences(mParkMePreference, Context.MODE_PRIVATE);

    }

    public static ParkMeSharedPreference getInstance(Context context) {

        if (instance == null) {
            instance = new ParkMeSharedPreference(context.getApplicationContext());
        }
        return instance;
    }

    public void setFbLogin(boolean isLoggedIn) {
        sharedpreferences.edit().putBoolean(FB_LOGGED_IN, isLoggedIn).apply();

        setIsLoggedIn(isLoggedIn);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        sharedpreferences.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedpreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public boolean isFbLoggedIn() {
        return sharedpreferences.getBoolean(FB_LOGGED_IN, false);
    }

    public void clearAll() {
        sharedpreferences.edit().remove(IS_LOGGED_IN).apply();

        sharedpreferences.edit().remove(FB_LOGGED_IN).apply();

        sharedpreferences.edit().remove(USER_NAME).apply();
        sharedpreferences.edit().remove(VEHICLE).apply();
        sharedpreferences.edit().remove(EMAIL).apply();

    }

    public void setEmail(String email) {
        sharedpreferences.edit().putString(EMAIL,email).apply();
    }
    public String getEmail(){
        return sharedpreferences.getString(EMAIL,null);
    }

    public void setVehicle(String vehicle) {
        sharedpreferences.edit().putString(VEHICLE,vehicle).apply();
    }

    public String getVehicle(){
        String vehicle= sharedpreferences.getString(VEHICLE,null);
        return (vehicle==null)?"KA01MC2525":vehicle;
    }

    public void setUsername(String name) {
        sharedpreferences.edit().putString(USER_NAME,name).apply();
    }

    public String getUserName(){
        return sharedpreferences.getString(USER_NAME,null);
    }
}
