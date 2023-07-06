package com.example.kishservices.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kishservices.R;
import com.example.kishservices.services.pojo.AnswersResponse;

import java.util.ArrayList;

public class AnswerRecyclerViewAdapter extends RecyclerView.Adapter<AnswerRecyclerViewAdapter.RecyclerViewHolder> {
    ArrayList<AnswersResponse> answersArrayList;
    Context context;
    private int lastCheckedPosition = RecyclerView.NO_POSITION;

    public interface OnAnswerClickListener{
        void AnswerClick(int answerId);
    }

    private OnAnswerClickListener onAnswerClickListener;

    public void setOnAnswerClickListener(OnAnswerClickListener onAnswerClickListener) {
        this.onAnswerClickListener = onAnswerClickListener;
    }

    public AnswerRecyclerViewAdapter(ArrayList<AnswersResponse> answersArrayList, Context context){
        this.answersArrayList = answersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnswerRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        AnswersResponse answersResponse = answersArrayList.get(position);
        holder.answerText.setText(answersResponse.answerText);

        // Set the checkbox state based on the isChecked field of ItemModel
        holder.checkBox.setChecked(answersResponse.isChecked());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkBox.setChecked(true);
                //pass the id of the checked answer to the the fragment
                if(onAnswerClickListener!=null){
                    onAnswerClickListener.AnswerClick(answersArrayList.get(lastCheckedPosition).id);
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Uncheck the previously checked item
                    int previousCheckedPosition = lastCheckedPosition;
                    if (previousCheckedPosition != RecyclerView.NO_POSITION) {
                        answersArrayList.get(previousCheckedPosition).setChecked(false);
                        notifyItemChanged(previousCheckedPosition);
                    }

                    // Update the current item as checked
                    answersResponse.setChecked(true);
                    lastCheckedPosition = position;


                } else {
                    // If the checkbox is unchecked, update the isChecked field of the current item
                    answersResponse.setChecked(false);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return answersArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView answerText ;
        CheckBox checkBox;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            answerText = itemView.findViewById(R.id.answer_choice);
            checkBox = itemView.findViewById(R.id.answer_checkbox);

        }
    }
}
