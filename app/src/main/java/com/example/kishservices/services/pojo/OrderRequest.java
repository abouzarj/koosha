package com.example.kishservices.services.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderRequest {

    public String explanation;
    public Date due_date;
    public String order_state;
    public int service;
    public ArrayList<Integer> candidate_supliers;
    public int chosen_supplier;
    public int customer;

}