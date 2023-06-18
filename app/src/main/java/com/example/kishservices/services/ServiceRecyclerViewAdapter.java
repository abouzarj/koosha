package com.example.kishservices.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kishservices.R;
import com.example.kishservices.services.pojo.Service;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class ServiceRecyclerViewAdapter extends RecyclerView.Adapter<ServiceRecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<Service> serviceArrayList;
    private Context context;

    public ServiceRecyclerViewAdapter(ArrayList<Service> serviceArrayList, Context context){
        this.serviceArrayList=serviceArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public ServiceRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        Service service = serviceArrayList.get(position);
        holder.serviceTitle.setText(service.title);
        URL url = service.image;
        Picasso.get().load(url.toString()).into(holder.serviceImage);
    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceTitle;
        private ShapeableImageView serviceImage;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceTitle = itemView.findViewById(R.id.service_title);
            serviceImage = itemView.findViewById(R.id.service_image);
        }
    }
}
