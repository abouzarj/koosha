package com.example.kishservices.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kishservices.R;
import com.example.kishservices.retrofit.APIClient;
import com.example.kishservices.retrofit.APIInterface;
import com.example.kishservices.services.pojo.AnswersResponse;
import com.example.kishservices.services.pojo.QuestionsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionFragment extends Fragment implements AnswerRecyclerViewAdapter.OnAnswerClickListener {
    private QuestionsResponse question;
    private Integer QApostion;

    RecyclerView recyclerView;
    AnswerRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    APIInterface apiInterface;
    SharedPreferences sharedPreferences;

    ArrayList<AnswersResponse> answersArrayList = new ArrayList<>();

    public interface OnDataPass {
        void onDataPassed(int answerId, int qaPosition);
    }

    private OnDataPass dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Make sure the activity implements the interface
        if (context instanceof OnDataPass) {
            dataPasser = (OnDataPass) context;
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
    private void sendDataToActivity(Integer answerId,int qaPosition) {
        if (dataPasser != null) {
            dataPasser.onDataPassed(answerId,qaPosition);
        }
    }


    public QuestionFragment(QuestionsResponse question, Integer QAposition) {
        // Required empty public constructor
        this.question = question;
        this.QApostion = QAposition;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        TextView questionText = rootView.findViewById(R.id.question_text);
        questionText.setText(question.questionText);

        recyclerView = rootView.findViewById(R.id.answers_recycler);
        adapter = new AnswerRecyclerViewAdapter(answersArrayList,getActivity());
        adapter.setOnAnswerClickListener(this::AnswerClick);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                layoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        getData();

        return rootView;
    }

    private void getData(){
        String b = "JWT "+ sharedPreferences.getString("access","");
        Call<ArrayList<AnswersResponse>> getAnswers = apiInterface.getAnswers(b,question.id);
        getAnswers.enqueue(new Callback<ArrayList<AnswersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AnswersResponse>> call, Response<ArrayList<AnswersResponse>> response) {
                if(response.isSuccessful()){
                    answersArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AnswersResponse>> call, Throwable t) {

            }
        });
    }

    @Override
    public void AnswerClick(int answerId) {
        sendDataToActivity(answerId,QApostion);
    }
}