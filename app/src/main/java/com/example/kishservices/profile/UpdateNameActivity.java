package com.example.kishservices.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kishservices.MainActivity;
import com.example.kishservices.R;
import com.example.kishservices.profile.pojo.UserInfoResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNameActivity extends AppCompatActivity {
    UserInfoResponse userInfoResponse;
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        userInfoResponse = (UserInfoResponse) getIntent().getSerializableExtra("info");

        EditText editText1 = findViewById(R.id.nach_edit);
        EditText editText2 = findViewById(R.id.laskj_edit);

        editText1.setText(userInfoResponse.firstName);
        editText2.setText(userInfoResponse.lastName);

        // Storing data into SharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        token = "JWT " + sharedPreferences.getString("access","");

        Button saveButton = findViewById(R.id.savench);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoResponse.firstName = String.valueOf(editText1.getText());
                userInfoResponse.lastName = String.valueOf(editText2.getText());
                updateUser();

            }
        });



    }

    void updateUser(){
        Call<UserInfoResponse> updateUserInfo = apiInterface.updateUserInfo(token, userInfoResponse);
        updateUserInfo.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful()){
                    int a = response.code();
                    displayDialog();

                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });
    }

    public void displayDialog(){
        Typeface typeface = ResourcesCompat.getFont(this,R.font.estedad);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setMessage("اطلاعات با موفقیت ذخیره شد");
        builder.setCancelable(true);





        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        TextView title = (TextView) new TextView(this);
        // You Can Customise your Title here
        title.setText("کوشا خدمت");
        title.setBackgroundColor(getResources().getColor(R.color.gr500));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(18);

        title.setTypeface(typeface);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.setCustomTitle(title);



        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.grc_700));                                    }
        });
        // Show the Alert Dialog box
        alertDialog.show();
    }
}