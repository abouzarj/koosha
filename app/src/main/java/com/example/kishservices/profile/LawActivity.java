package com.example.kishservices.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kishservices.R;
import com.example.kishservices.profile.pojo.TermResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    String token;

    TextView termsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);


        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        token = "JWT " + sharedPreferences.getString("access","");

        termsTextView = findViewById(R.id.textView2);

        //get term of use
        getTermsOfUse();

    }

    public void getTermsOfUse(){
        Call<ArrayList<TermResponse>> getTerms = apiInterface.getTerm(token);
        getTerms.enqueue(new Callback<ArrayList<TermResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TermResponse>> call, Response<ArrayList<TermResponse>> response) {
                if(response.isSuccessful()){
                    termsTextView.setText(response.body().get(0).content);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TermResponse>> call, Throwable t) {

            }
        });
    }
}