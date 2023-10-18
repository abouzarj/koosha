package com.example.kishservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ConnectivityManagerCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Storing data into SharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if(sharedPreferences.contains("logged")){
//                    if(sharedPreferences.getBoolean("logged", false)){
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                    }else if( !sharedPreferences.getBoolean("logged", false) ){
//                        Intent intent  = new Intent(MainActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                    }
//
//                }else if(!sharedPreferences.contains("logged")){
//                    Intent intent  = new Intent(MainActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        };
//
//        handler.postDelayed(runnable,2000);


    }


}