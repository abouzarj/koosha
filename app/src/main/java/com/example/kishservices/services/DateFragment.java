package com.example.kishservices.services;

import static android.content.ContentValues.TAG;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kishservices.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.hamsaa.persiandatepicker.util.PersianCalendarUtils;


public class DateFragment extends Fragment {

    Date myDate;
    int mHour,mMinute;

    boolean isDateSet = false, isTimeSet= false;

    private ExtendedFloatingActionButton nextButton;

    public interface OnDatePass{
        void onDatePassed(java.util.Date date);


    }

    private OnDatePass datePasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the activity implements the interface
        if (context instanceof DateFragment.OnDatePass) {
            datePasser = (DateFragment.OnDatePass) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataPass interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datePasser = null;
    }

    // To pass the data to the activity, call this method where needed
    public void sendDateToActivity( ) {
        if (datePasser != null) {

            datePasser.onDatePassed(myDate);
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
        View rootView = inflater.inflate(R.layout.fragment_date, container, false);
        ImageButton dateButton = rootView.findViewById(R.id.calender_btn);
        TextView dateText = rootView.findViewById(R.id.date_text);
        ImageButton timeButton = rootView.findViewById(R.id.time_btn);
        TextView timeTextView = rootView.findViewById(R.id.time_text);
        MaterialCardView dateCardView = rootView.findViewById(R.id.date_card_view);
        MaterialCardView timeCardView = rootView.findViewById(R.id.time_card_view);




        PersianDatePickerDialog picker = new PersianDatePickerDialog(getActivity())
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setTitleColor(getResources().getColor(R.color.grc_700))
                .setMinYear(1300)
                .setInitDate(1370, 3, 13)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(false)
                .setListener(new PersianPickerListener() {
                    @Override
                    public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {
                        Log.d(TAG, "onDateSelected: " + persianPickerDate.getTimestamp());//675930448000
                        Log.d(TAG, "onDateSelected: " + persianPickerDate.getGregorianDate());//Mon Jun 03 10:57:28 GMT+04:30 1991
                        Log.d(TAG, "onDateSelected: " + persianPickerDate.getPersianLongDate());// دوشنبه  13  خرداد  1370
                        Log.d(TAG, "onDateSelected: " + persianPickerDate.getPersianMonthName());//خرداد
                        Log.d(TAG, "onDateSelected: " + PersianCalendarUtils.isPersianLeapYear(persianPickerDate.getPersianYear()));//true
                        Toast.makeText(getActivity(), persianPickerDate.getPersianYear() + "/" + persianPickerDate.getPersianMonth() + "/" + persianPickerDate.getPersianDay(), Toast.LENGTH_SHORT).show();
                        dateText.setText(persianPickerDate.getPersianYear() + "/" + persianPickerDate.getPersianMonth() + "/" + persianPickerDate.getPersianDay());
                        myDate =  persianPickerDate.getGregorianDate();
                        isDateSet= true;
                    }

                    @Override
                    public void onDismissed() {

                    }
                });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dateCardView.setStrokeWidth(2);
                dateCardView.setStrokeColor(getResources().getColor(R.color.white));

                picker.show();
            }
        });

        PersianCalendar persianCalendar = new PersianCalendar();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String hourString   = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String time         =  hourString + ":" + minuteString;
                timeTextView.setText(time);
                mHour = hourOfDay;
                mMinute = minute;
                isTimeSet = true;


            }
        },persianCalendar.HOUR_OF_DAY,persianCalendar.MINUTE,true);





        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCardView.setStrokeWidth(2);
                timeCardView.setStrokeColor(getResources().getColor(R.color.white));
                timePickerDialog.show();
            }
        });

        nextButton = rootView.findViewById(R.id.date_next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isDateSet){
                    dateCardView.setStrokeWidth(2);
                    dateCardView.setStrokeColor(getResources().getColor(R.color.grc_700));


                }

                if(!isTimeSet){
                    timeCardView.setStrokeWidth(2);
                    timeCardView.setStrokeColor(getResources().getColor(R.color.grc_700));
                }

                if (isDateSet && isTimeSet){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(myDate);
                    calendar.set(Calendar.HOUR_OF_DAY,mHour);
                    calendar.set(Calendar.MINUTE,mMinute);
                    myDate = calendar.getTime();

                    sendDateToActivity();
                }








            }
        });

        return rootView;
    }
}