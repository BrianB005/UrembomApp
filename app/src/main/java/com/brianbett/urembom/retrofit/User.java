package com.brianbett.urembom.retrofit;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user")
    UserDetails userDetails;
    String token;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public String getToken() {
        return token;
    }
}
