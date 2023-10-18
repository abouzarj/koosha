package com.example.kishservices.services.pojo;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CustomerResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user")
    @Expose
    public User user;

}