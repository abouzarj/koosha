package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SearchActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

    ServicesResponse servicesResponse;

    ArrayList<Service> serviceArrayList= new ArrayList<>();

    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    ServiceRecyclerViewAdapter adapter;
    LinearLayout noInternetLayout;

    String searchWord = " ";
    Integer currentPage= 1;
    Integer totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.search_view);

        //Get ImageView of icon
        ImageView searchViewIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);

        //Get parent of gathered icon
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        //Remove it from the left...
        linearLayoutSearchView.removeView(searchViewIcon);
        //then put it back (to the right by default)
        linearLayoutSearchView.addView(searchViewIcon);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        recyclerView = findViewById(R.id.service_recycler_view);
        adapter = new ServiceRecyclerViewAdapter(serviceArrayList,this);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        noInternetLayout = findViewById(R.id.search_no_internet);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                serviceArrayList.clear();
                adapter.notifyDataSetChanged();
                searchWord = query;
                checkInternetConnection(searchWord,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                serviceArrayList.clear();
                adapter.notifyDataSetChanged();
                searchWord = newText;
                checkInternetConnection(searchWord,1);
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(SearchActivity.this, "Last", Toast.LENGTH_LONG).show();
                    currentPage++;
                    if(currentPage<=totalPages){

                        checkInternetConnection(searchWord,currentPage);
                    }

                }
            }
        });




    }

    private void getData(String query, Integer pageNumber){
        query = query.trim();
        if (query.isEmpty()) return;
        Call<ServicesResponse> servicesResponseCall = apiInterface.SearchServices(sharedPreferences.getString("access","").toString(),query.trim(),pageNumber);
        servicesResponseCall.enqueue(new Callback<ServicesResponse>() {
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

    private void checkInternetConnection(String query, Integer pageNumber){
        //check internet connectivity
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            noInternetLayout.setVisibility(View.GONE);
            //get the collection list from the server
            getData(query,pageNumber);
        }else {
            noInternetLayout.setVisibility(View.VISIBLE);
        }

    }
}