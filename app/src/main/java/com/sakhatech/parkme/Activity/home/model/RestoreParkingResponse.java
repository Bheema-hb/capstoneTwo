package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.ParkMeResponse;

/**
 * Created by Bheema
 * Company Techjini
 */
public class RestoreParkingResponse extends ParkMeResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("slotNumber")
        public int slotNumber;
        @SerializedName("available_lots")
        public int availableSlots;
        @SerializedName("parkingId")
        public String carParkId;
        @SerializedName("isParkingInProgress")
        public boolean isParkingInProgress;
        @SerializedName("car_park_area")
        public String parkingArea;
        @SerializedName("type_of_payment")
        public String paymentType;
        @SerializedName("vehicle")
        public String vehicle;
        @SerializedName("parkingStartedTime")
        public long parkingStartedTime;
        @SerializedName("park_lot_type")
        public String parkLotType;
        @SerializedName("sunday_rate")
        public String sundayRate;
        @SerializedName("saturday_rate")
        public String saturdayRate;
        @SerializedName("public_holiday_rate")
        public String holidayRate;
        @SerializedName("ca_park_weekday_rate")
        public String weekdayRate;
    }


}
