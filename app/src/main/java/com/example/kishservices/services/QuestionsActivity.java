package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.kishservices.HomeActivity;
import com.example.kishservices.R;
import com.example.kishservices.services.pojo.QARequest;
import com.example.kishservices.services.pojo.QuestionsResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity implements QuestionFragment.OnDataPass {
    private ViewPagerAdapter viewPagerAdapter;
    private SwipeControlViewPager viewPager;
    private TabLayout tabLayout;

    ArrayList<QuestionsResponse> questionList;

    ArrayList<QARequest> qaRequestArrayList = new ArrayList<>();

    int serviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setSwipeEnabled(false);

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Intent myIntent = getIntent();
        questionList = (ArrayList<QuestionsResponse>) myIntent.getSerializableExtra("questions");

        if( myIntent.hasExtra("service_id")){
            serviceID = myIntent.getIntExtra("service_id",0);
        }

        for(int i=0; i<questionList.size();i++){
            QARequest qaRequest = new QARequest();
            qaRequest.question= questionList.get(i).id;
            qaRequestArrayList.add(qaRequest);
            viewPagerAdapter.add(new QuestionFragment(questionList.get(i),i), "page 1");

        }

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setMax(viewPager.getAdapter().getCount()+ 4);
        progressBar.setProgress(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(position + 1);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int count = viewPagerAdapter.getCount();
        Intent intent = new Intent(this, OrderActivity.class);

        ExtendedFloatingActionButton nextButton = findViewById(R.id.next_question);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int b = viewPager.getCurrentItem();
                if(b == count-1){
                    intent.putExtra("qas",qaRequestArrayList);
                    intent.putExtra("service_id", serviceID);
                    startActivity(intent);

                }else{
                    viewPager.swipeToNextPage();
                }


            }
        });

        ImageView closeButton = findViewById(R.id.close_icon);
        Intent intent1 = new Intent(this, HomeActivity.class);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        int b = viewPager.getCurrentItem();
        if(b == 0){
            super.onBackPressed();
        }else{
            viewPager.swipeToPreviousPage();
        }

    }


    @Override
    public void onDataPassed(int answerId, int qaPosition) {
        qaRequestArrayList.get(qaPosition).answer=answerId;

    }
}