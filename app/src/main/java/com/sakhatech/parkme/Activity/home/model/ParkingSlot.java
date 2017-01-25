package com.sakhatech.parkme.Activity.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bheema
 * Company Techjini
 */
public class ParkingSlot implements Serializable {
    @SerializedName("car_park_name")
    public String carParkName;
    @SerializedName("singapore_date")
    public long time;
    @SerializedName("ca_park_weekday_rate")
    public String weekdayRate;
    @SerializedName("car_park_area")
    public String parkingArea;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("no_of_motor_car_lot")
    public int totalLot;
    @SerializedName("available_lots")
    public int availableSlots;
    @SerializedName("park_lot_type")
    public String parkLotType;
    @SerializedName("sunday_rate")
    public String sundayRate;
    @SerializedName("saturday_rate")
    public String saturdayRate;
    @SerializedName("public_holiday_rate")
    public String holidayRate;
    @SerializedName("park_id")
    public long parkId;


    //controling variables
    @SerializedName("isVehicle")
    public boolean isVehicle;
    @SerializedName("slotNumber")
    public String slotNumber;
    @SerializedName("isParked")
    public boolean isParked;
    @SerializedName("parkingStartedTime")
    public long parkingStartedTime;
    @SerializedName("parkingEndTime")
    public long parkingEndTime;
    @SerializedName("vehicle")
    public String vehicle;
    @SerializedName("isParkingEnd")
    public boolean isParkingEnd;
}
