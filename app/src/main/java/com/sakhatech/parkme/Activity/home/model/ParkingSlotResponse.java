package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.ParkMeResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bheema
 * Company Techjini
 */
public class ParkingSlotResponse extends ParkMeResponse {
    @SerializedName("data")
    public Data data;

    public class Data implements Serializable{
        @SerializedName("statuscode")
        public int statuscode;
        @SerializedName("success")
        public boolean success;
        @SerializedName("slots")
        public ArrayList<ParkingSlot> slots;
    }
}
