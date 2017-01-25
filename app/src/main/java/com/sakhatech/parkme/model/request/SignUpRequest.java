package com.sakhatech.parkme.model.request;

import com.sakhatech.parkme.ParkMeApplication;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class SignUpRequest extends SpotizenRequest {


    public Data data;
    public String file;


    public class Data {
        public String email;
        public String fbToken;
        public String loginType;
        public String mobile;
        public String password;
        public String userName;
        public String vehicle;

    }

    @Override
    public String getJsonString() {
        return ParkMeApplication.getInstance().getmGson().toJson(this);

    }
}
