package com.example.kishservices.services.pojo;

import java.net.URL;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class ServicesResponse {

    @SerializedName("page_size")
    @Expose
    public Integer pageSize;
    @SerializedName("total_objects")
    @Expose
    public Integer totalObjects;
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;
    @SerializedName("current_page_number")
    @Expose
    public Integer currentPageNumber;
    @SerializedName("next")
    @Expose
    public String next;
    @SerializedName("previous")
    @Expose
    public Object previous;
    @SerializedName("results")
    @Expose
    public List<Service> results;

}