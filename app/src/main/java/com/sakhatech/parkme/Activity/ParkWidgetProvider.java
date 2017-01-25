package com.sakhatech.parkme.Activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.sakhatech.parkme.data.Contract;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.spotizen.R;

/**
 * Created by Bheema on 1/25/17.
 */

public class ParkWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cursor = context.getContentResolver().query(Contract.Record.uri, null, null, null, null);

        for (int widgetId : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            if (cursor != null && cursor.moveToFirst()) {
//
//                remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.vehicle_number_text) + " " + ParkMeSharedPreference.getInstance(context).getVehicle());
//                remoteViews.setTextViewText(R.id.slot_number, context.getString(R.string.slot_number_text) + " " + cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_SLOT_NUMBER)));
//                remoteViews.setTextViewText(R.id.vehicle_parked_duration, Utility.getSpentDuration(System.currentTimeMillis() - Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.Record.PARK_START_TIME)))) );
//
//            } else {
//                remoteViews.setTextViewText(R.id.vehicle_number, context.getString(R.string.no_vehicle_parked));
//                remoteViews.setTextViewText(R.id.slot_number, "");
//                remoteViews.setTextViewText(R.id.vehicle_parked_duration, "" );
//
//            }

            Intent intent = new Intent(context, SplashScreen.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_parent, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
