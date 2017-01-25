package com.sakhatech.parkme.model;

import java.io.Serializable;

/**
 * Created by Bheema.
 * <p/>
 * Company Techjini
 */
public class Device implements Serializable {
    private String deviceId;
    private Location location;
    private int appVersion;
    private Platform platform;


    public String getId() {
        return deviceId;
    }

    public void setId(String id) {
        this.deviceId = id;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

}
