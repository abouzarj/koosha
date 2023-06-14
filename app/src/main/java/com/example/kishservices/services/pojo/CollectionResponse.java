package com.example.kishservices.services.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URL;


public class CollectionResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("image")
    @Expose
    public URL image;

}
