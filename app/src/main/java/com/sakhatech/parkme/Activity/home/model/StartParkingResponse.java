package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.ParkMeResponse;

/**
 * Created by Bheema
 * Company Techjini
 */
public class StartParkingResponse extends ParkMeResponse {

    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("parkingStartedTime")
        public long parkingStartedTime;
        @SerializedName("slotNumber")
        public int slotNumber;
        @SerializedName("parkingId")
        public String parkingId;
        @SerializedName("isParkingInProgress")
        public boolean isParkingInProgress;
        @SerializedName("vehicle")
        public String vehicle;

    }

}
