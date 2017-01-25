package com.sakhatech.parkme.model.request;


import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.Device;

import java.io.Serializable;

/**
 * Created by bheema.
 */
public abstract class SpotizenRequest implements Serializable {
    @SerializedName("device")
    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }


    abstract public String getJsonString();

}
