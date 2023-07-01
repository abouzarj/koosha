package com.example.kishservices.retrofit;

import com.example.kishservices.pojo.LoginResponse;
import com.example.kishservices.pojo.UpdatePassResponse;
import com.example.kishservices.pojo.Username;
import com.example.kishservices.services.pojo.CollectionResponse;
import com.example.kishservices.services.pojo.QuestionsResponse;
import com.example.kishservices.services.pojo.ServicesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @FormUrlEncoded
    @POST("servicemanager/register/")
    Call<Username> register(@Field("username") String username, @Field("password") String password, @Field("phone") String phone, @Field("first_name") String first_name, @Field("last_name") String last_name);

    @FormUrlEncoded
    @POST("auth/jwt/create/")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("servicemanager/updatepass/change_password/")
    Call<UpdatePassResponse> updatepass(@Field("phone") String phone);


    @GET("servicemanager/collections")
    Call<ArrayList<CollectionResponse>> getCollections();

   @Headers({
           "Accept: application/json",
   })
    @GET("servicemanager/services")
    Call<ServicesResponse> SearchServices(@Header("Authorization") String authorization, @Query("search") String search,@Query("page") Integer page,@Query("collection_id") Integer collection_id);

    @Headers({
            "Accept: application/json",
    })
    @GET("servicemanager/questions")
    Call<ArrayList<QuestionsResponse>> getQuestions(@Header("Authorization") String authorization, @Query("service_id") Integer service_id);


}
