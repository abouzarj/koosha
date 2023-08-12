package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kishservices.R;
import com.example.kishservices.services.pojo.OrderRequest;
import com.example.kishservices.services.pojo.QARequest;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class OrderActivity extends AppCompatActivity implements ExplanationFragment.OnDataPass, AddressFragment.OnAddressPass,DateFragment.OnDatePass,FinalFragment.OnOrderPass{
    ArrayList<QARequest> qaRequestArrayList;
    private ViewPagerAdapter viewPagerAdapter;
    private SwipeControlViewPager viewPager;
    private TabLayout tabLayout;

    OrderRequest orderRequest = new OrderRequest();

    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        String qid = "qid= ", aid = "aid= ";
        Intent intent = getIntent();
        if(intent.hasExtra("qas")){
            qaRequestArrayList = (ArrayList<QARequest>) intent.getSerializableExtra("qas");
        }


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
                if(currentFragment instanceof FinalFragment){
                    TextView exp_title = currentFragment.getView().findViewById(R.id.exp_text);
                    exp_title.setText(orderRequest.explanation);

                    TextView dateText = currentFragment.getView().findViewById(R.id.date_text);
                    PersianDate persianDate = new PersianDate(orderRequest.due_date);
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



    public void goToNextPage(){
        int count = viewPagerAdapter.getCount();
        int b = viewPager.getCurrentItem();
        if (b == count - 1) {
            String s = orderRequest.explanation;
            Date d = orderRequest.due_date;
            int qw = 98;
        } else if (b == count - 2) {

            viewPager.swipeToNextPage();
        } else {
            viewPager.swipeToNextPage();
        }
    }

    @Override
    public void onDatePassed(Date date) {
        orderRequest.due_date=date;
        goToNextPage();
    }

    @Override
    public void OrderPassed() {

        goToNextPage();
    }
}