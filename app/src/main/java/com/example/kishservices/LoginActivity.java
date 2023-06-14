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
import android.widget.TextView;

import com.example.kishservices.pojo.UpdatePassResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView textView = findViewById(R.id.new_acount_tv);
        EditText phoneText = findViewById(R.id.phone_ed);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        TextView textView1 = findViewById(R.id.no_internet_text);
        MaterialCardView materialCardView = findViewById(R.id.mcv);

        //check if the app is connected to internet or not
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        Button button = (Button) findViewById(R.id.rb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get details on the currently active default data network
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                textView1.setVisibility(View.GONE);
                materialCardView.setStrokeColor(getResources().getColor(R.color.white));

                if(TextUtils.isEmpty(phoneText.getText().toString())){
                    //progressBar.setVisibility(View.GONE);
                    materialCardView.setStrokeColor(getResources().getColor(R.color.grc_700));
                    return;
                }

                if(isConnected){
                    Call<UpdatePassResponse> updatePassResponseCall = apiInterface.updatepass(phoneText.getText().toString().trim());
                    updatePassResponseCall.enqueue(new Callback<UpdatePassResponse>() {
                        @Override
                        public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {

                            if(response.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,GetCodeActivity.class);
                                intent.putExtra("username",response.body().username);
                                Log.i("updatePassResponseCall","inside successful response ");

                                startActivity(intent);
                            }

                            if(response.code() == 404){
                                textView1.setText("حساب کاربری با این شماره تلفن پیدا نشد");
                                textView1.setVisibility(View.VISIBLE);
                                materialCardView.setStrokeColor(getResources().getColor(R.color.grc_700));

                            }

                        }

                        @Override
                        public void onFailure(Call<UpdatePassResponse> call, Throwable t) {

                        }
                    });

                }else {
                    textView1.setText("لطفا اتصال خود به اینترنت را بررسی کنید");
                    textView1.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}