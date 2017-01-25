package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.ParkMeApplication;
import com.sakhatech.parkme.model.request.SpotizenRequest;

/**
 * Created by Bheema
 * Company Techjini
 */
public class StartParkingRequest extends SpotizenRequest {
    @SerializedName("data")
    public Data data;


    public class Data {
        @SerializedName("parkingId")
        public String parkingId;
        @SerializedName("slotNumber")
        public int slotNumber;
        @SerializedName("vehicle")
        public String vehicle;

        @Override
        public String toString() {
            return "{\"parkingId\":\"" + parkingId + "\",\"slotNumber\":" + slotNumber +",\"vehicle\":\"" + vehicle +"\"}";
        }
    }


    @Override
    public String getJsonString() {
        return ParkMeApplication.getInstance().getmGson().toJson(this);
    }
}
