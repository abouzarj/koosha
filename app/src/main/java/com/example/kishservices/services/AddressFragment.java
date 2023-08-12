package com.example.kishservices.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.kishservices.R;
import com.example.kishservices.databinding.FragmentAddressBinding;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddressFragment extends Fragment {
    private GoogleMap mMap;
    private Marker mMarker;
    double latitude;
    double longitude;
    int userId;
    String readAbleAddress;
    AddressRequest currentAddress;
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    String token;
    private ExtendedFloatingActionButton nextButton;


    public interface OnAddressPass{
        void onAddressPassed(String address);
    }

    private OnAddressPass addressPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the activity implements the interface
        if (context instanceof OnAddressPass) {
            addressPasser  = (OnAddressPass) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataPass interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addressPasser = null;
    }

    public void sendAddressToActivity( ) {
        if (addressPasser != null) {
            addressPasser.onAddressPassed(readAbleAddress);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentAddressBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
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
        });

        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        token = "JWT " + sharedPreferences.getString("access","");

        nextButton = binding.addressNextBtn;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMarker != null){
                    //save lat and long
                    latitude = mMarker.getPosition().latitude;
                    longitude = mMarker.getPosition().longitude;
                    //get readable address
                    getReadableAddress();
                }



            }
        });

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void   getReadableAddress( ){


        Call<ReverseGeocodeResponse> geocodeResponseCall = apiInterface.get_readable_address(token, latitude, longitude);
        geocodeResponseCall.enqueue(new Callback<ReverseGeocodeResponse>() {
            @Override
            public void onResponse(Call<ReverseGeocodeResponse> call, Response<ReverseGeocodeResponse> response) {
                readAbleAddress = response.body().formattedAddress;
                sendAddressToActivity();
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

//    private void getCurrentAddress() {
//        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//        token = "JWT " + sharedPreferences.getString("access","");
//
//
//        Call<ArrayList<AddressRequest>> getAddress = apiInterface.getAddress(token);
//        getAddress.enqueue(new Callback<ArrayList<AddressRequest>>() {
//            @Override
//            public void onResponse(Call<ArrayList<AddressRequest>> call, Response<ArrayList<AddressRequest>> response) {
////                addressText.setText(response.body().get(0).addressText);
////                model.getLiveData().setValue(response.body().get(0).addressText);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<AddressRequest>> call, Throwable t) {
//
//            }
//        });
//    }
}