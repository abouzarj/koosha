package com.example.kishservices.services.pojo;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressRequest extends MutableLiveData<AddressRequest> {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("address_text")
    @Expose
    public String addressText;
    @SerializedName("user")
    @Expose
    public Integer user;

}