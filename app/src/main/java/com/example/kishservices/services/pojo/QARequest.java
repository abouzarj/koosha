package com.example.kishservices.services.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QARequest implements Serializable {

    @SerializedName("order")
    @Expose
    public Integer order;
    @SerializedName("question")
    @Expose
    public Integer question;
    @SerializedName("answer")
    @Expose
    public Integer answer;

}