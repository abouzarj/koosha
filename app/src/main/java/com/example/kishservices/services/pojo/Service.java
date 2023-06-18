package com.example.kishservices.services.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Service {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("image")
    @Expose
    public URL image;
    @SerializedName("collection")
    @Expose
    public Integer collection;

}

