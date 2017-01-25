package com.sakhatech.parkme.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sakhatech.parkme.Activity.ParkWidgetProvider;

import java.util.Calendar;

/**
 * Created by Bheema on 1/25/17.
 */

public class AppWidgetAlarm {
    private final int ALARM_ID = 0;
    private final int INTERVAL_MILLIS = 5000;

    private Context mContext;


    public AppWidgetAlarm(Context context)
    {
        mContext = context;
    }


    public void startAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS);

        Intent alarmIntent = new Intent(ParkWidgetProvider.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), INTERVAL_MILLIS, pendingIntent);
    }


    public void stopAlarm()
    {
        Intent alarmIntent = new Intent(ParkWidgetProvider.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}