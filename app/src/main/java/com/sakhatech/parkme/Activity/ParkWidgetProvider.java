package com.sakhatech.parkme.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.sakhatech.parkme.data.Contract;
import com.sakhatech.parkme.util.AppWidgetAlarm;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.spotizen.R;

/**
 * Created by Bheema on 1/25/17.
 */

public class ParkWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("","onUpdate widget");
        Cursor cursor = context.getContentResolver().query(Contract.Record.uri, null, null, null, null);

        for (int widgetId : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            if (cursor != null && cursor.moveToFirst()) {

                remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.vehicle_number_text) + " " + ParkMeSharedPreference.getInstance(context).getVehicle());
                remoteViews.setTextViewText(R.id.slot_number, context.getString(R.string.slot_number_text) + " " + cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_SLOT_NUMBER)));
                remoteViews.setTextViewText(R.id.vehicle_parked_duration, Utility.getSpentDuration(System.currentTimeMillis() - Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_START_TIME)))) );

            } else {
                remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.no_vehicle_parked));
                remoteViews.setTextViewText(R.id.slot_number, "");
                remoteViews.setTextViewText(R.id.vehicle_parked_duration, "" );

            }

            Intent intent = new Intent(context, SplashScreen.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_parent, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        Log.e("","Onrecive widget");
        if(intent.getAction().equals(ACTION_AUTO_UPDATE))
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            Cursor cursor = context.getContentResolver().query(Contract.Record.uri, null, null, null, null);

            for (int widgetId : appWidgetIds) {

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                if (cursor != null && cursor.moveToFirst()) {

                    remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.vehicle_number_text) + " " + ParkMeSharedPreference.getInstance(context).getVehicle());
                    remoteViews.setTextViewText(R.id.slot_number, context.getString(R.string.slot_number_text) + " " + cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_SLOT_NUMBER)));
                    remoteViews.setTextViewText(R.id.vehicle_parked_duration, Utility.getSpentDuration(System.currentTimeMillis() - Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_START_TIME)))) );

                } else {
                    remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.no_vehicle_parked));
                    remoteViews.setTextViewText(R.id.slot_number, "");
                    remoteViews.setTextViewText(R.id.vehicle_parked_duration, "" );

                }

                Intent clickIntent = new Intent(context, SplashScreen.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.widget_parent, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }

    }

    @Override
    public void onEnabled(Context context)
    {
        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context)
    {
        // TODO: alarm should be stopped only if all widgets has been disabled

        // stop alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }
}
