package com.yang.app1028.retrofit.gerrit;

import com.yang.app1028.retrofit.gerrit.Change;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GerritAPI {
    @GET("changes/")
    Call<List<Change>> loadChanges(@Query("q") String status);

//    @GET("user")
//    Call<UserDetails> getUserDetails(@Header("Authorization") String credentials);

    String credential = Credentials.basic("ausername", "apassword");

    @POST("change")
    Call<Change> postChange(@Body Change change);
}
