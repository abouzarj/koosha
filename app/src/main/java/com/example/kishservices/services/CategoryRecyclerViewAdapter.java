package com.example.kishservices.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kishservices.R;
import com.example.kishservices.services.pojo.CollectionResponse;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<CollectionResponse> collectionResponseArrayList;
    private Context mContext;

    public CategoryRecyclerViewAdapter(ArrayList<CollectionResponse> collectionResponseArrayList, Context mContext){
        this.collectionResponseArrayList= collectionResponseArrayList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        CollectionResponse collectionResponse = collectionResponseArrayList.get(position);
        holder.categoryText.setText(collectionResponse.title);
        URL url = collectionResponse.image;
        Picasso.get().load(url.toString()).into(holder.categoryImage);

    }

    @Override
    public int getItemCount() {
        return collectionResponseArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryText;
        private ShapeableImageView categoryImage;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.category_text);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }
}
