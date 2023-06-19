package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kishservices.R;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.Service;
import com.example.kishservices.services.pojo.ServicesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    APIInterface apiInterface;
    ServiceRecyclerViewAdapter adapter;

    ArrayList<Service> serviceArrayList  = new ArrayList<>();

    LinearLayout noInternetLayout;

    int collection_id;

    private String access;

    Integer currentPage= 1;
    Integer totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Intent myIntent = getIntent();
        collection_id = myIntent.getIntExtra("collection_id", 0);
        String title = myIntent.getStringExtra("title");
        TextView textView = findViewById(R.id.c_title);
        textView.setText(title);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        access = sharedPreferences.getString("accsess"," ");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        recyclerView = findViewById(R.id.services_recyclerView);
        adapter = new ServiceRecyclerViewAdapter(serviceArrayList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        noInternetLayout = findViewById(R.id.services_no_internet);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        checkInternetConnection();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    if(currentPage<=totalPages){

                        checkInternetConnection();
                    }

                }
            }
        });


        Button button = findViewById(R.id.services_try_again_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnection();
            }
        });







    }

    private void getData(){
        Call<ServicesResponse> getServices = apiInterface.SearchServices(access,"",currentPage,collection_id);
        getServices.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {
                if(response.isSuccessful()){
                    currentPage = response.body().currentPageNumber;
                    totalPages = response.body().totalPages;
                    serviceArrayList.addAll(response.body().results);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

            }
        });
    }

    private void checkInternetConnection(){
        //check internet connectivity
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            noInternetLayout.setVisibility(View.GONE);
            //get the collection list from the server
            getData();
        }else {
            noInternetLayout.setVisibility(View.VISIBLE);
        }
    }
}