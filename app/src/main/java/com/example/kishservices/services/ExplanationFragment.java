package com.example.kishservices.services;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.kishservices.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class ExplanationFragment extends Fragment  {

    private ExtendedFloatingActionButton nextButton;

    public EditText explanationEditText;




    public interface OnDataPass {
        void onDataPassed(String data);

    }

    private ExplanationFragment.OnDataPass dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the activity implements the interface
        if (context instanceof ExplanationFragment.OnDataPass) {
            dataPasser = (ExplanationFragment.OnDataPass) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataPass interface.");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        dataPasser = null;
    }

    // To pass the data to the activity, call this method where needed
    public void sendDataToActivity( ) {
        if (dataPasser != null) {
            dataPasser.onDataPassed(explanationEditText.getText().toString());
        }
    }





    public ExplanationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_explanation, container, false);

        explanationEditText = rootView.findViewById(R.id.expl_edt);

        nextButton = rootView.findViewById(R.id.explanation_next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToActivity();
            }
        });




        return rootView;
    }




}