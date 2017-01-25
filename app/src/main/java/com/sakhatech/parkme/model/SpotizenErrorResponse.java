package com.sakhatech.parkme.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by techjini.
 */
public class SpotizenErrorResponse implements Serializable {

    @SerializedName("statuscode")
    public int statuscode;
    @SerializedName("message")
    public String message;
}

