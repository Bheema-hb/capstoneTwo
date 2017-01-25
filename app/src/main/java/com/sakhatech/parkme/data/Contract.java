package com.sakhatech.parkme.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String AUTHORITY = "nanodegree.com.capstone";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECORD = "ParkRecords";

    public static final class Record implements BaseColumns {

        public static final Uri uri = BASE_URI.buildUpon().appendPath(PATH_RECORD).build();

        public static final String TABLE_NAME = "ParkRecords";


        public static final String PARK_NAME = "name";
        public static final String PARK_START_TIME = "parkStartTime";
        public static final String PARK_SLOT_NUMBER = "parkSlotNumber";
        public static final String PARK_AREA = "parkArea";
        public static final String PARK_AVAILABLE_SLOTS = "parkAvailableSlots";
        public static final String PARK_LATITUDE = "latitude";
        public static final String PARK_LONGITUDE = "longitude";
        public static final String PARK_ID = "parkId";

//
//        public static final String[] PARK_COLUMNS = {
//                _ID,
//                PARK_NAME,
//                PARK_START_TIME, PARK_SLOT_NUMBER, PARK_AREA, PARK_AVAILABLE_SLOTS, PARK_LATITUDE, PARK_LONGITUDE, PARK_ID
//        };



    }

}
