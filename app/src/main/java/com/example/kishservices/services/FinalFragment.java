package com.example.kishservices.services;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kishservices.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class FinalFragment extends Fragment {

    private ExtendedFloatingActionButton oderButton;

    public interface OnOrderPass{
        void OrderPassed();
    }

    OnOrderPass orderPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the activity implements the interface
        if (context instanceof FinalFragment.OnOrderPass) {
            orderPasser = (FinalFragment.OnOrderPass) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataPass interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        orderPasser = null;
    }

    // To pass the data to the activity, call this method where needed
    public void sendOrderToActivity( ) {
        if (orderPasser != null) {
            orderPasser.OrderPassed();
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
        View rootView = inflater.inflate(R.layout.fragment_final, container, false);

        oderButton = rootView.findViewById(R.id.final_order_btn);
        oderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToActivity();
            }
        });

        return rootView;
    }

    public void sendOrder(){

    }
}