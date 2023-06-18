package com.example.kishservices.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kishservices.R;
import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.CollectionResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesCategoryFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    Fragment fragment;
    RecyclerView recyclerView;
    ArrayList<CollectionResponse> collectionResponseArrayList = new ArrayList<>();
    APIInterface apiInterface;

    CategoryRecyclerViewAdapter adapter;

    private View rootView = null;

    ProgressBar progressBar;

    LinearLayout noInternetLayout;

    public ServicesCategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when OrdersFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finishAffinity();
                getActivity().finish();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_services_category, container, false);

            // Storing data into SharedPreferences
            sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
            myEdit = sharedPreferences.edit();

            recyclerView = rootView.findViewById(R.id.cat_recycler);
            adapter = new CategoryRecyclerViewAdapter(collectionResponseArrayList,getActivity());
            int mNoOfColumns = Utility.calculateNoOfColumns(getActivity(),180);
            GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),mNoOfColumns);

            progressBar = rootView.findViewById(R.id.collection_progress);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            noInternetLayout = rootView.findViewById(R.id.collection_no_internet);
            Button tryAgainButton = rootView.findViewById(R.id.try_again_button);
            tryAgainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkInternetConnection();

                }
            });

            checkInternetConnection();

            RelativeLayout searchBarLayout = rootView.findViewById(R.id.collection_search_bar);
            searchBarLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    startActivity(intent);

                }
            });

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.collection_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternetConnection();
                swipeRefreshLayout.setRefreshing(false);
                collectionResponseArrayList.clear();
                adapter.notifyDataSetChanged();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void checkInternetConnection(){
        //check internet connectivity
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            noInternetLayout.setVisibility(View.GONE);
            //get access from the server
            getAccess();

            //get the collection list from the server
            getData();
        }else {
            noInternetLayout.setVisibility(View.VISIBLE);
        }

    }

    private void getAccess(){
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
    }

   private void getData(){
       progressBar.setVisibility(View.VISIBLE);

       Call<ArrayList<CollectionResponse>> arrayListCall = apiInterface.getCollections();
       arrayListCall.enqueue(new Callback<ArrayList<CollectionResponse>>() {
           @Override
           public void onResponse(Call<ArrayList<CollectionResponse>> call, Response<ArrayList<CollectionResponse>> response) {
               if(response.isSuccessful()){
                   progressBar.setVisibility(View.GONE);
                   collectionResponseArrayList.addAll(response.body());
                   adapter.notifyDataSetChanged();


               }
           }

           @Override
           public void onFailure(Call<ArrayList<CollectionResponse>> call, Throwable t) {

           }
       });
   }

}