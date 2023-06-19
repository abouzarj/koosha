package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kishservices.R;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Intent myIntent = getIntent();
        int a = myIntent.getIntExtra("collection_id",0);

        TextView textView = findViewById(R.id.cid);
        textView.setText(String.valueOf(a));

    }
}