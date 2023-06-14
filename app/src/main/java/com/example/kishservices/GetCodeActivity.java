package com.example.kishservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCodeActivity extends AppCompatActivity {
    MySMSBroadcastReceiver mySMSBroadcastReceiver;
    private static final int SMS_CONSENT_REQUEST = 1;
    EditText codeEditText;
    String password;
    String username;

    APIInterface apiInterface;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);

        Button enterButton = findViewById(R.id.btn_enter);
        TextView textView = findViewById(R.id.countdown_text);
        Intent myIntent = getIntent();
        //Initiate SMS Retrieval
        StartSMSRetriever();

        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                textView.setText(f.format(min) + ":" + f.format(sec));

            }

            @Override
            public void onFinish() {
                textView.setText("00:00");
            }
        }.start();

        codeEditText = findViewById(R.id.code_edittext);

        // Storing data into SharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        myEdit = sharedPreferences.edit();

        //get username from intent and save it in shared preferences
        username = myIntent.getStringExtra("username");
        myEdit.putString("username", username);





        //get an instance of retrofit client
        apiInterface = APIClient.getClient().create(APIInterface.class);


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<LoginResponse> loginResponseCall = apiInterface.login(username,codeEditText.getText().toString());
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()){
                            myEdit.putBoolean("logged",true);
                            myEdit.putString("password",codeEditText.getText().toString());
                            myEdit.putString("access", response.body().access);
                            myEdit.commit();
                            Intent intent = new Intent(GetCodeActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }
        });



    }

    void StartSMSRetriever(){
        SmsRetrieverClient client= SmsRetriever.getClient(this);
        client.startSmsUserConsent("+989981000176").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Start smsRetriever", "on success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(" Start smsRetriever", "on success");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SMS_CONSENT_REQUEST){
            if(resultCode==RESULT_OK && data!=null){
                String message=data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Toast.makeText(GetCodeActivity.this,message , Toast.LENGTH_SHORT).show();
                getOtpFromMessage(message);
                Log.i("on activity result", "got the message");
            }
        }
    }

    void registerMySMSBroadcastReceiver(){
        mySMSBroadcastReceiver = new MySMSBroadcastReceiver();
        mySMSBroadcastReceiver.smsBroadcastListener = new MySMSBroadcastReceiver.SMSBroadcastListener() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent,SMS_CONSENT_REQUEST);
                Log.i("register smsRetriever", "on success");
            }

            @Override
            public void onError() {
                Log.i(" Register smsRetriever", "on error");

            }
        };
        IntentFilter intentFilter=new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(mySMSBroadcastReceiver,intentFilter);
    }

    private void getOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(|^)\\d{5}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            codeEditText.setText(matcher.group(0));
            password = matcher.group(0);
            myEdit.putString("password",password);
            myEdit.commit();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerMySMSBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mySMSBroadcastReceiver);
    }
}