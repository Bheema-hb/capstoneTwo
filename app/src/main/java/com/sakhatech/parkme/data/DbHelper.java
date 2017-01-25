package com.sakhatech.parkme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {


    static final String NAME = "ParkMe.db";
    private static final int VERSION = 1;


    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String builder = "CREATE TABLE " + Contract.Record.TABLE_NAME + " (" +
                Contract.Record._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Record.PARK_NAME + " TEXT NOT NULL, " +
                Contract.Record.PARK_START_TIME + " TEXT NOT NULL, " +
                Contract.Record.PARK_SLOT_NUMBER + " TEXT NOT NULL, " +
                Contract.Record.PARK_AREA + " TEXT NOT NULL, " +
                Contract.Record.PARK_AVAILABLE_SLOTS + " TEXT NOT NULL, " +
                Contract.Record.PARK_LATITUDE + " TEXT NOT NULL, " +
                Contract.Record.PARK_LONGITUDE + " TEXT NOT NULL, " +
                Contract.Record.PARK_ID + " LONG NOT NULL )";

        db.execSQL(builder);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + Contract.Record.TABLE_NAME);

        onCreate(db);
    }
}
