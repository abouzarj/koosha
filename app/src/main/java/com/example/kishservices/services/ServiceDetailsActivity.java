package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kishservices.R;
import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.QuestionsResponse;
import com.example.kishservices.services.pojo.Service;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

    String access;

    ArrayList<QuestionsResponse> questionList;
    boolean noQuestions;

    Intent myIntent;

    SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Service service = (Service) getIntent().getSerializableExtra("object");

        ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar)).setTitle(service.title);

        ImageView imageView = findViewById(R.id.s_image);
        URL url = service.image;
        Picasso.get().load(url.toString()).into(imageView);

        TextView textView = findViewById(R.id.service_description);
        textView.setText(service.description);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        myEdit = sharedPreferences.edit();

        Call<LoginResponse> loginResponseCall = apiInterface.login(sharedPreferences.getString("username",""),sharedPreferences.getString("password",""));
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    Log.i("loginResponseCall","inside successful response ");

                    myEdit.putString("access",response.body().access);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

        access = sharedPreferences.getString("access","");
        String  b = "JWT " + access;

        Call<ArrayList<QuestionsResponse>> questionsResponseCall = apiInterface.getQuestions(b,service.id);
        questionsResponseCall.enqueue(new Callback<ArrayList<QuestionsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<QuestionsResponse>> call, Response<ArrayList<QuestionsResponse>> response) {
                if(response.isSuccessful()){
                    if(response.body().isEmpty()){
                        noQuestions = true;
                    }else {
                        noQuestions = false;
                        questionList = response.body();
                    }

                }

            }

            @Override
            public void onFailure(Call<ArrayList<QuestionsResponse>> call, Throwable t) {
                int g = 9;
            }
        });

        Button button = findViewById(R.id.order_begin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(noQuestions){
                    myIntent = new Intent(getBaseContext(), OrderActivity.class);
                    startActivity(myIntent);
                }else {
                    myIntent = new Intent(getBaseContext(), QuestionsActivity.class);
                    startActivity(myIntent);
                }

            }
        });











    }
}