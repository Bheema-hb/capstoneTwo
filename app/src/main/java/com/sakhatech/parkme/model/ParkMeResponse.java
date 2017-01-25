package com.sakhatech.parkme.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class ParkMeResponse implements Serializable {

    @SerializedName("error")
    private SpotizenErrorResponse error;
    @SerializedName("meta")
    private MetaData meta;

    public SpotizenErrorResponse getError() {
        return error;
    }

    public void setError(SpotizenErrorResponse error) {
        this.error = error;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }
}
