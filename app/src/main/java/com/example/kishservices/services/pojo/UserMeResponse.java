package com.example.kishservices.services.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserMeResponse {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("username")
    @Expose
    public String username;

}