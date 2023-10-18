package com.example.kishservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.kishservices.orders.OrdersFragment;
import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.profile.ProfileFragment;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.ServicesCategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    APIInterface apiInterface;

    BottomNavigationView bottomNavigationView;

    ProfileFragment profileFragment = new ProfileFragment();
    ServicesCategoryFragment servicesCategoryFragment = new ServicesCategoryFragment();
    OrdersFragment ordersFragment = new OrdersFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //get an instance of retrofit client
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Storing data into SharedPreferences
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();


        bottomNavigationView = findViewById(R.id.bottom_nave);
        //        Call<LoginResponse> loginResponseCall = apiInterface.login(sharedPreferences.getString("username",""),sharedPreferences.getString("password",""));

        Call<LoginResponse> loginResponseCall = apiInterface.login("abouzar","wxkxbqx8");
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    Log.i("loginResponseCall","inside successful response ");

                    myEdit.putString("access",response.body().access);
                    myEdit.apply();
                    int a = 90;
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });



        //set default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,servicesCategoryFragment,null).commit();
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile_menu_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFragment,null).commit();
                        return true;
                    case R.id.services_menu_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,servicesCategoryFragment,null).commit();
                        return true;
                    case R.id.orders_menu_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ordersFragment,null).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}