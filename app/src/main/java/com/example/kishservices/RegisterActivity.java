package com.example.kishservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kishservices.pojo.Username;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    APIInterface apiInterface;
    Intent intent;
    TextView rUserTextView;
    TextView rPhoneTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button) findViewById(R.id.register);
        //get an instance of api
        apiInterface = APIClient.getClient().create(APIInterface.class);
        EditText username = findViewById(R.id.username_edit);
        EditText phone = findViewById(R.id.phone_edit);
        intent = new Intent(RegisterActivity.this, GetCodeActivity.class);
        TextView textView1 = findViewById(R.id.no_internet_text1);
        rUserTextView = findViewById(R.id.r_userame);
        rPhoneTextView = findViewById(R.id.r_phone);
        ProgressBar progressBar =findViewById(R.id.register_progress);
        MaterialCardView materialCardView = findViewById(R.id.mat);
        MaterialCardView materialCardView1 = findViewById(R.id.mat1);


        //check if the app is connected to internet or not
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rUserTextView.setVisibility(View.GONE);
                rPhoneTextView.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                materialCardView.setStrokeColor(getResources().getColor(R.color.white));
                materialCardView1.setStrokeColor(getResources().getColor(R.color.white));

                // Get details on the currently active default data network
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(TextUtils.isEmpty(username.getText().toString())){
                    //progressBar.setVisibility(View.GONE);
                    materialCardView.setStrokeColor(getResources().getColor(R.color.grc_700));
                    return;
                }

                if(TextUtils.isEmpty(phone.getText().toString())){
                    //progressBar.setVisibility(View.GONE);
                    materialCardView1.setStrokeColor(getResources().getColor(R.color.grc_700));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);



                if(isConnected){
                    Call<Username> register = apiInterface.register(username.getText().toString().trim(),"",phone.getText().toString().trim(),"","");
                    register.enqueue(new Callback<Username>() {
                        @Override
                        public void onResponse(Call<Username> call, Response<Username> response) {
                            progressBar.setVisibility(View.GONE);
                            Log.i("response",String.valueOf(response.code()));

                            if(response.code()==201){
                                String username = response.body().username;
                                intent.putExtra("username",username);
                                startActivity(intent);
                            }
                            if(response.code()==400){
                                try {
                                    String a =  response.errorBody().string();
                                    if(a.contains("username")){
                                        rUserTextView.setVisibility(View.VISIBLE);
                                        materialCardView.setStrokeColor(getResources().getColor(R.color.grc_700));
                                    }
                                    if(a.contains("phone")){
                                        rPhoneTextView.setVisibility(View.VISIBLE);
                                        materialCardView1.setStrokeColor(getResources().getColor(R.color.grc_700));
                                    }
                                    Log.i("a",a);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Username> call, Throwable t) {

                        }
                    });

                }else {
                    textView1.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }



            }
        });

    }
}