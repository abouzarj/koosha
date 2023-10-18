package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kishservices.HomeActivity;
import com.example.kishservices.R;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.CustomerResponse;
import com.example.kishservices.services.pojo.OrderPostRequest;
import com.example.kishservices.services.pojo.OrderRequest;
import com.example.kishservices.services.pojo.QARequest;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class OrderActivity extends AppCompatActivity implements ExplanationFragment.OnDataPass, AddressFragment.OnAddressPass,DateFragment.OnDatePass,FinalFragment.OnOrderPass{
    SharedPreferences sharedPreferences;
    APIInterface apiInterface;
    String token;

    ArrayList<QARequest> qaRequestArrayList;
    private ViewPagerAdapter viewPagerAdapter;
    private SwipeControlViewPager viewPager;
    private TabLayout tabLayout;

    OrderPostRequest orderRequest = new OrderPostRequest();

    String address;

    ProgressBar sendOrderProgress;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        sendOrderProgress = findViewById(R.id.send_order_progress);
        ImageView orderCloseIcon = findViewById(R.id.order_close_icon);



        intent = getIntent();
        if(intent.hasExtra("qas")){
            qaRequestArrayList = (ArrayList<QARequest>) intent.getSerializableExtra("qas");
        }

        if(intent.hasExtra("service_id")){
            orderRequest.service= intent.getIntExtra("service_id",0);
        }

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        token = "JWT " + sharedPreferences.getString("access","");


        viewPager = findViewById(R.id.order_viewpager);
        viewPager.setSwipeEnabled(false);

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new ExplanationFragment(), "توضیحات");
        viewPagerAdapter.add(new AddressFragment(), "آدرس");
        viewPagerAdapter.add(new DateFragment(), "تاریخ");
        viewPagerAdapter.add(new FinalFragment(), "ثبت نهایی");

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        tabLayout = findViewById(R.id.order_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        ProgressBar progressBar = findViewById(R.id.order_progress_horizontal);
        if(intent.hasExtra("qas")){
            progressBar.setMax(qaRequestArrayList.size() + 4);
            progressBar.setProgress(qaRequestArrayList.size() + 1);
        }else {
            progressBar.setMax(4);
            progressBar.setProgress(1);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(intent.hasExtra("qas")){
                    progressBar.setProgress(position + qaRequestArrayList.size() + 1);
                }else{
                    progressBar.setProgress(position +  1);

                }

                Fragment currentFragment = viewPagerAdapter.getItem(viewPager.getCurrentItem());
                if(currentFragment instanceof FinalFragment) {
                    TextView exp_title = currentFragment.getView().findViewById(R.id.exp_text);
                    exp_title.setText(orderRequest.explanation);

                    TextView dateText = currentFragment.getView().findViewById(R.id.date_text);

                    Date d = null;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(orderRequest.dueDate);
                        }
                    } catch (ParseException e) {

                    }
                    assert d != null;
                    PersianDate persianDate = new PersianDate(d);
                    PersianDateFormat persianDateFormat = new PersianDateFormat();
                    dateText.setText(persianDateFormat.format(persianDate, "l j F Y H:i"));

                    TextView addressText = currentFragment.getView().findViewById(R.id.addre_text);
                    addressText.setText(address);


                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        orderCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OrderActivity.this, HomeActivity.class);
                startActivity(intent1);
            }
        });




    }

    @Override
    public void onBackPressed() {

        int b = viewPager.getCurrentItem();
        if (b == 0) {
            super.onBackPressed();
        } else {
            viewPager.swipeToPreviousPage();
        }

    }

    //receives explanation text from Explanation Fragment
    @Override
    public void onDataPassed(String data) {
        orderRequest.explanation = data;
        goToNextPage();
    }


    //receives address text from Address Fragment
    @Override
    public void onAddressPassed(String address) {
        //update customer address
         this.address = address;
        goToNextPage();
    }



    public void goToNextPage()  {
        int count = viewPagerAdapter.getCount();
        int b = viewPager.getCurrentItem();
        if (b == count - 1) {
            String s = orderRequest.explanation;
            try {
                Date d = new SimpleDateFormat().parse(orderRequest.dueDate);
            }catch (ParseException e){

            }


        } else if (b == count - 2) {

            viewPager.swipeToNextPage();
        } else {
            viewPager.swipeToNextPage();
        }
    }



    @Override
    public void onDatePassed(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        String formattedDate = outputFormat.format(date);

        orderRequest.dueDate=formattedDate;
        goToNextPage();
    }

    @Override
    public void OrderPassed() {
       getCustomer();
       goToNextPage();
    }

    public void sendOrder(){

        orderRequest.orderState="AC";
        orderRequest.candidateSupliers = new ArrayList<>();
        orderRequest.chosenSupplier = null;

        Call<OrderRequest> createOrder = apiInterface.createOrder(token, orderRequest);
        createOrder.enqueue(new Callback<OrderRequest>() {
            @Override
            public void onResponse(Call<OrderRequest> call, Response<OrderRequest> response) {

                if(response.isSuccessful()){
                    int orderId = response.body().id;
                    if (intent.hasExtra("qas")){
                        sendQAs(orderId);
                    }else {
                        sendOrderProgress.setVisibility(View.GONE);
                        showDialog();
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderRequest> call, Throwable t) {

            }
        });
    }

    public void getCustomer(){
        sendOrderProgress.setVisibility(View.VISIBLE);
        Call<CustomerResponse> getCustomer = apiInterface.getCustomer(token);
        getCustomer.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                orderRequest.customer=response.body().id;
                sendOrder();

            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {

            }
        });

    }

    public void sendQAs(int orderID){


        for(int i=0; i< qaRequestArrayList.size(); i++){
            qaRequestArrayList.get(i).order=orderID;
            Call<QARequest> createQA=apiInterface.sendQa(token, qaRequestArrayList.get(i));
            int finalI = i;
            createQA.enqueue(new Callback<QARequest>() {
                @Override
                public void onResponse(Call<QARequest> call, Response<QARequest> response) {
                    if (finalI == qaRequestArrayList.size()-1){
                        //show a dialog that says order has been successfully saved into server
                        sendOrderProgress.setVisibility(View.GONE);
                        showDialog();

                    }
                }

                @Override
                public void onFailure(Call<QARequest> call, Throwable t) {

                }
            });
        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this,R.style.MyDialogTheme);
        builder.setMessage("سفارش شما ثبت گردید.");
        builder.setCancelable(false);


        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        TextView title = (TextView) new TextView(OrderActivity.this);
        // You Can Customise your Title here
        title.setText("کوشا خدمت");
        title.setBackgroundColor(getResources().getColor(R.color.gr500));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(18);

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