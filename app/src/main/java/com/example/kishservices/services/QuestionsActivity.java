package com.example.kishservices.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.kishservices.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class QuestionsActivity extends AppCompatActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private SwipeControlViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setSwipeEnabled(false);

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // add the fragments
        viewPagerAdapter.add(new QuestionFragment(), "Page 1");
        viewPagerAdapter.add(new QuestionFragment(), "Page 2");
        viewPagerAdapter.add(new QuestionFragment(), "Page 3");

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setMax(viewPager.getAdapter().getCount());
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
                    startActivity(intent);

                }else{
                    viewPager.swipeToNextPage();
                }


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
}