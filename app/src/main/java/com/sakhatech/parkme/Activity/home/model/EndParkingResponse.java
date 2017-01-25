package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.ParkMeResponse;

/**
 * Created by Bheema
 * Company Techjini
 */
public class EndParkingResponse extends ParkMeResponse {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("billDetail")
        public Bill billDetail;

    }

    public class Bill {
        @SerializedName("currentTime")
        public long currentTime;
        @SerializedName("amountTopay")
        public double amountTopay;
        @SerializedName("parkingStartedTime")
        public long parkingStartedTime;
        @SerializedName("totalTimeParked")
        public double totalTimeParked;

    }
}
