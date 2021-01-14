package com.yang.app1028.retrofit.gerrit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yang.app1028.retrofit.gerrit.Change;
import com.yang.app1028.retrofit.gerrit.GerritAPI;

import java.io.IOException;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller implements Callback<List<Change>> {
    static final String BASE_URL = "https://git.eclipse.org/r/";
    static final String LOCAL_URL = "http://localhost:4567/";

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();

        Gson gson2 = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LOCAL_URL)
                .addConverterFactory(GsonConverterFactory.create(gson2))
                .build();
        GerritAPI gerritAPI = retrofit.create(GerritAPI.class);

        //1. GET
//        Call<List<Change>> call = gerritAPI.loadChanges("status:open");
//        call.enqueue(this); //即callback处理response

        //2. POST
        Change change = new Change();
        change.setSubject("New change.");

        Call<Change> call2 = gerritAPI.postChange(change);
        call2.enqueue(new Callback<Change>() {
            @Override
            public void onResponse(Call<Change> call, Response<Change> response) {
                // 已经转换为想要的类型了
                Change result = response.body();
                Log.d("yang", result.toString());
            }

            @Override
            public void onFailure(Call<Change> call, Throwable t) {
                t.printStackTrace();
            }
        });


        //OkHttp: 怎么在Header addInterceptor,为了加入Authorization
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder()
                        .header("Authorization", Credentials.basic("aUsername", "aPassword"));
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://api.example.com")
                .client(okHttpClient)
                .build();
    }

    @Override
    public void onResponse(Call<List<Change>> call, Response<List<Change>> response) {
        if (response.isSuccessful()) {
            List<Change> changeList = response.body();
            for (Change item : changeList) {
                Log.d("yang", item.subject);
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Change>> call, Throwable t) {
        t.printStackTrace();
    }
}
