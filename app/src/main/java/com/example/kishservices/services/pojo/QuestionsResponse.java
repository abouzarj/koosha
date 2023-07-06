package com.example.kishservices.services.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionsResponse implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("service")
    @Expose
    public Integer service;
    @SerializedName("question_text")
    @Expose
    public String questionText;

}