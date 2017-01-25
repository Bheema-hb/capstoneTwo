package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.ParkMeApplication;
import com.sakhatech.parkme.model.request.SpotizenRequest;

/**
 * Created by Bheema
 * Company Techjini
 */
public class EndParkingRequest extends SpotizenRequest {

    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("parkingId")
        public String parkingId;
    }


    @Override
    public String getJsonString() {
        return ParkMeApplication.getInstance().getmGson().toJson(this);
    }
}
