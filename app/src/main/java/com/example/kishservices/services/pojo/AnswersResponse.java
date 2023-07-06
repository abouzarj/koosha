package com.example.kishservices.services.pojo;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswersResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("question")
    @Expose
    public List<Integer> question;
    @SerializedName("answer_text")
    @Expose
    public String answerText;

    public boolean isChecked = false;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}