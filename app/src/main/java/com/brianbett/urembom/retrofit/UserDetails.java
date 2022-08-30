package com.brianbett.urembom.retrofit;

import com.google.gson.annotations.SerializedName;

public class UserDetails {
    @SerializedName("name")
    String username;

    public String getUsername() {
        return username;
    }
}
