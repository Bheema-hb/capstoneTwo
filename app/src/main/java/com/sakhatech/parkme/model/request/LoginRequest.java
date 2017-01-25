package com.sakhatech.parkme.model.request;

import com.google.gson.annotations.SerializedName;
import com.sakhatech.parkme.ParkMeApplication;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class LoginRequest extends SpotizenRequest {
  public Data data;


    public class Data{
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;

        @Override
        public String toString() {

            return "{\"email\":\""+email+"\",\"password\":"+"\""+password+"\"}";

        }

    }

    @Override
    public String toString() {
        return "{\"data\":"+data.toString()+"}";
    }

    @Override
    public String getJsonString() {
        return ParkMeApplication.getInstance().getmGson().toJson(this);

    }
}
