package com.yang.app1028.retrofit.youdao;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.yang.app1028.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YouDaoActivity extends AppCompatActivity {
    private static final String TAG = YouDaoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        initRetrofit();
    }

    private void initRetrofit() {
        //4.创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) // 支持Gson解析
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
//              .addConverterFactory(ProtoConverterFactory.create()) // 支持Prototocobuff解析

        // 5.创建 网络请求接口 的实例
        GetRequestService service = retrofit.create(GetRequestService.class);

        //对 发送请求 进行封装
//        Call<Translation> call = service.getCall();
        Call<Translation1> call1 = service.postCall("I love you");

        //6.发送网络请求(异步)
        call1.enqueue(new Callback<Translation1>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation1> call, Response<Translation1> response) {
                // 对返回数据进行处理
                if (response.isSuccessful()) {
                    String result = response.body().getTranslateResult().get(0).get(0).getTgt();
                    Log.d(TAG, result);
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<Translation1> call, Throwable throwable) {
                Log.d(TAG, "连接失败");
            }
        });


        // 其他具体使用
        // @FormUrlEncoded
        Call<ResponseBody> call11 = service.testFormUrlEncoded1("Carson", 24);

        // @FieldMap
        // 实现的效果与上面相同，但要传入Map
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Carson");
        map.put("age", 24);
        Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);

        /** @Multipart */
        MediaType textType = MediaType.parse("text/plain");
        RequestBody name = RequestBody.create(textType, "Carson");
        RequestBody age = RequestBody.create(textType, "24");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容");

        // @Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);

        // @PartMap
        // 实现和上面同样的效果
        Map<String, RequestBody> fileUpload2Args = new HashMap<>();
        fileUpload2Args.put("name", name);
        fileUpload2Args.put("age", age);
        //这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但上面的 filePart 有
//        fileUpload2Args.put("file", file);
        Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //单独处理文件

    }

}