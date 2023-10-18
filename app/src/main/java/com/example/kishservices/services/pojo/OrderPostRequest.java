package com.example.kishservices.services.pojo;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderPostRequest {

    @SerializedName("explanation")
    @Expose
    public String explanation;
    @SerializedName("due_date")
    @Expose
    public String dueDate;
    @SerializedName("order_state")
    @Expose
    public String orderState;
    @SerializedName("service")
    @Expose
    public Integer service;
    @SerializedName("candidate_supliers")
    @Expose
    public List<Integer> candidateSupliers;
    @SerializedName("chosen_supplier")
    @Expose
    public Integer chosenSupplier;
    @SerializedName("customer")
    @Expose
    public Integer customer;

}