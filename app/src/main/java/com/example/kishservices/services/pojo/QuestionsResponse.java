package com.example.kishservices.services.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionsResponse {

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