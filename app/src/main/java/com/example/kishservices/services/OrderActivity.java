package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kishservices.R;
import com.example.kishservices.services.pojo.QARequest;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        String qid= "qid= ",aid="aid= ";
        Intent intent = getIntent();
        ArrayList<QARequest> qaRequestArrayList = (ArrayList<QARequest>) intent.getSerializableExtra("qas");
        for(int i=0; i<qaRequestArrayList.size();i++){
            qid = qid + qaRequestArrayList.get(i).question + "\n";
            aid = aid + qaRequestArrayList.get(i).answer +"\n";

        }


    }
}