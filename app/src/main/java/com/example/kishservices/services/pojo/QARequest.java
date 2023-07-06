package com.example.kishservices.services.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QARequest implements Serializable {

    @SerializedName("order")
    @Expose
    public Object order;
    @SerializedName("question")
    @Expose
    public Object question;
    @SerializedName("answer")
    @Expose
    public Object answer;

}