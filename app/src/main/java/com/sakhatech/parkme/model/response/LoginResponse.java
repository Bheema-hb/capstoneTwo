package com.sakhatech.parkme.model.response;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.model.ParkMeResponse;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class LoginResponse extends ParkMeResponse {
    public Data data;

    public class Data {
        @SerializedName("statuscode")
        public int statuscode;
        @SerializedName("success")
        public boolean success;
        @SerializedName("sessionId")
        public String sessionId;
        @SerializedName("username")
        public String username;
        @SerializedName("message")
        public String message;
        @SerializedName("email")
        public String email;
    }
}
