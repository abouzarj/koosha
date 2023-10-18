package com.example.kishservices.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kishservices.HomeActivity;
import com.example.kishservices.MainActivity;
import com.example.kishservices.R;
import com.example.kishservices.profile.pojo.UserInfoResponse;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.OrderActivity;
import com.example.kishservices.services.ServicesCategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    Fragment fragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    APIInterface apiInterface;

    String token;

    TextView name,phone,username;

    ProgressBar progressBar;

    UserInfoResponse userInfoResponse;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when OrdersFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                fragment = new ServicesCategoryFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment).commit();

                BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_nave);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        name = rootView.findViewById(R.id.fl_name);
        phone = rootView.findViewById(R.id.user_phone);
        username = rootView.findViewById(R.id.usernamee);
        progressBar = rootView.findViewById(R.id.profile_progressbar);

        // Storing data into SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        myEdit = sharedPreferences.edit();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        token = "JWT " + sharedPreferences.getString("access","");

        //get user info
        getUserInfo();

        RelativeLayout nameLayout = rootView.findViewById(R.id.name_layout);
        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), UpdateNameActivity.class);
                intent.putExtra("info", userInfoResponse);

                startActivity(intent);
            }
        });

        RelativeLayout exitRelativeLayout = rootView.findViewById(R.id.exit_acount);
        exitRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display dialog
                displayDialog();
            }
        });

        RelativeLayout lawRelativeLayout = rootView.findViewById(R.id.law_rel);
        lawRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LawActivity.class);
                startActivity(intent);
            }
        });


        return  rootView;
    }

    public void getUserInfo(){
        progressBar.setVisibility(View.VISIBLE);
        Call<UserInfoResponse> getUserInformation = apiInterface.getUserInfo(token);
        getUserInformation.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    userInfoResponse = response.body();
                    name.setText(userInfoResponse.firstName + " "+  userInfoResponse.lastName);
                    phone.setText(userInfoResponse.phone);
                    username.setText(userInfoResponse.username);
                }

            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });

    }

    public void displayDialog(){
        Typeface typeface = ResourcesCompat.getFont(getActivity(),R.font.estedad);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        builder.setMessage("خارج میشوید؟ ");
        builder.setCancelable(true);





        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myEdit.putBoolean("logged",false);
                myEdit.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        TextView title = (TextView) new TextView(getActivity());
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