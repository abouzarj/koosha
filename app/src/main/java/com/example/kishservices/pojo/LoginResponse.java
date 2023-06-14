package com.example.kishservices.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("refresh")
    @Expose
    public String refresh;
    @SerializedName("access")
    @Expose
    public String access;
}
