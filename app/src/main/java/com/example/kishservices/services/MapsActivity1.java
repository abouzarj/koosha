package com.example.kishservices.services;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kishservices.R;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.AddressRequest;
import com.example.kishservices.services.pojo.ReverseGeocodeResponse;
import com.example.kishservices.services.pojo.UserMeResponse;
import com.example.kishservices.services.viewmodels.MyViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.kishservices.databinding.ActivityMaps1Binding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Marker mMarker;


    SharedPreferences sharedPreferences;
    APIInterface apiInterface;

    String token;

    double latitude;
    double longitude;


    int userId;

    String readAbleAddress;



    AddressRequest currentAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_maps1);
        }catch (Exception e){
            Log.e("TAG", "onCreateView", e);
            throw e;
        }




//        binding = ActivityMaps1Binding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        token = "JWT " + sharedPreferences.getString("access","");

        ExtendedFloatingActionButton button = findViewById(R.id.location_save_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMarker != null){
                    //save lat and long
                    latitude = mMarker.getPosition().latitude;
                    longitude = mMarker.getPosition().longitude;
                    //get readable address
                    getReadableAddress();


                    onBackPressed();
                }

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a listener to track camera movement
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Update marker position based on the new center LatLng
                if (mMarker != null) {
                    LatLng newLatLng = mMap.getCameraPosition().target;
                    mMarker.setPosition(newLatLng);

                }
            }
        });

        // Add a marker at the center of the screen
        LatLng centerLatLng = mMap.getCameraPosition().target;
        LatLng kish = new LatLng(26.539845,54.006674);

        MarkerOptions markerOptions = new MarkerOptions().position(centerLatLng);
        mMarker = mMap.addMarker(new MarkerOptions().position(kish).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kish));

    }

    public void   getReadableAddress( ){


        Call<ReverseGeocodeResponse> geocodeResponseCall = apiInterface.get_readable_address(token, latitude, longitude);
        geocodeResponseCall.enqueue(new Callback<ReverseGeocodeResponse>() {
            @Override
            public void onResponse(Call<ReverseGeocodeResponse> call, Response<ReverseGeocodeResponse> response) {
                readAbleAddress = response.body().formattedAddress;
                getUserId();

            }

            @Override
            public void onFailure(Call<ReverseGeocodeResponse> call, Throwable t) {

            }
        });




    }

    public void   getCurrentAddress(){


        Call<ArrayList<AddressRequest>> getAddress = apiInterface.getAddress(token);
        getAddress.enqueue(new Callback<ArrayList<AddressRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<AddressRequest>> call, Response<ArrayList<AddressRequest>> response) {
                if(response.body().isEmpty()){
                    createAddress();
                }else {
                    int address_id = response.body().get(0).id;
                    updateAddress(address_id);

                }

            }

            @Override
            public void onFailure(Call<ArrayList<AddressRequest>> call, Throwable t) {
                int  k = 9;
            }
        });



    }

    public void getUserId(){


        Call<UserMeResponse> getUser = apiInterface.getUser(token);
        getUser.enqueue(new Callback<UserMeResponse>() {
            @Override
            public void onResponse(Call<UserMeResponse> call, Response<UserMeResponse> response) {
                userId = response.body().id;
                getCurrentAddress();
            }

            @Override
            public void onFailure(Call<UserMeResponse> call, Throwable t) {

            }
        });


    }

    public void createAddress(){


        Call<AddressRequest> createAddress = apiInterface.createAddress(token,String.valueOf(latitude),String.valueOf(longitude),readAbleAddress,userId);
        createAddress.enqueue(new Callback<AddressRequest>() {
            @Override
            public void onResponse(Call<AddressRequest> call, Response<AddressRequest> response) {
                int a = response.code();
                AddressRequest currentAddress = response.body();
            }

            @Override
            public void onFailure(Call<AddressRequest> call, Throwable t) {

            }
        });
    }

    public void updateAddress(int address_id){
        Call<AddressRequest> updateAddress = apiInterface.updateAddress(token,address_id,String.valueOf(latitude),String.valueOf(longitude),readAbleAddress,userId);
        updateAddress.enqueue(new Callback<AddressRequest>() {
            @Override
            public void onResponse(Call<AddressRequest> call, Response<AddressRequest> response) {
                currentAddress = response.body();

            }

            @Override
            public void onFailure(Call<AddressRequest> call, Throwable t) {


            }
        });
    }







}