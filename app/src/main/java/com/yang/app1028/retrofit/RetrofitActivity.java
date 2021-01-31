package com.yang.app1028.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yang.app1028.R;
import com.yang.app1028.retrofit.stackOverflow.Answer;
import com.yang.app1028.retrofit.stackOverflow.ListWrapper;
import com.yang.app1028.retrofit.stackOverflow.Question;
import com.yang.app1028.retrofit.stackOverflow.RecyclerViewAdapter;
import com.yang.app1028.retrofit.stackOverflow.StackOverflowAPI;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "http://www.weather.com.cn/adat/sk/101010100.html";
    private static final int MSG_REQUEST = 0;
    private static final int MSG_UPDATE_UI = -1;

    //activity_okhttp
    private Button mBtRequest;
    private Button mBtRequestAsync;
    private TextView mTvResult;
    private BackgroundHandler mBackgroundHandler;
    private MainHandler mMainHandler;

    private StackOverflowAPI stackOverflowAPI;
    private String token;
    private Spinner questionsSpinner;
    private Button authenticateButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
    }

    /**
     * StackOverflow part
     */
    private void initStackOverflowPage() {
        questionsSpinner = findViewById(R.id.questions_spinner);
        questionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity2.this, "Spinner item selected", Toast.LENGTH_LONG).show();
                Question question = (Question) (parent.getAdapter().getItem(position));
                stackOverflowAPI.getAnswersForQuestion(question.question_id).enqueue(answersCallback);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        List<Question> questions = FakeDataProvider.getQuestions();
//        ArrayAdapter<Question> arrayAdapter = new ArrayAdapter<>(
//                MainActivity2.this, android.R.layout.simple_spinner_dropdown_item, questions);
//        questionsSpinner.setAdapter(arrayAdapter);

        authenticateButton = findViewById(R.id.authenticate_button);
        recyclerView = findViewById(R.id.questions_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        List<Answer> answers = FakeDataProvider.getAnswers();
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(answers);
//        recyclerView.setAdapter(adapter);
        createStackOverflowAPI();
        stackOverflowAPI.getQuestions().enqueue(questionsCallback);
    }

    private void createStackOverflowAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//todo:要这个干嘛
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StackOverflowAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        stackOverflowAPI = retrofit.create(StackOverflowAPI.class); //重要
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (token != null) {
            authenticateButton.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.text1:
                if (token != null) {
                    //todo
                } else {
                    Toast.makeText(RetrofitActivity.this, "Authenticate first.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.authenticate_button:
                //todo
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("yang", "MainActivity2 onActivityResult() called.");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            token = data.getStringExtra("token");
        }
    }

    //Define callbacks.
    Callback<ListWrapper<Question>> questionsCallback = new Callback<ListWrapper<Question>>() {
        @Override
        public void onResponse(Call<ListWrapper<Question>> call, Response<ListWrapper<Question>> response) {
            if (response.isSuccessful()) {
                ListWrapper<Question> questions = response.body();
                ArrayAdapter<Question> arrayAdapter = new ArrayAdapter<Question>(
                        RetrofitActivity.this, android.R.layout.simple_spinner_dropdown_item, questions.items);
                questionsSpinner.setAdapter(arrayAdapter);
            } else {
                Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ListWrapper<Question>> call, Throwable t) {
            t.printStackTrace();
        }
    };

    Callback<ListWrapper<Answer>> answersCallback = new Callback<ListWrapper<Answer>>() {
        @Override
        public void onResponse(Call<ListWrapper<Answer>> call, Response<ListWrapper<Answer>> response) {
            if (response.isSuccessful()) {
                List<Answer> data = new ArrayList<>();
                data.addAll(response.body().items);
                recyclerView.setAdapter(new RecyclerViewAdapter(data));
            } else {
                Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ListWrapper<Answer>> call, Throwable t) {
            t.printStackTrace();
        }
    };

    //TODO: 还没用过
    Callback<ResponseBody> upvoteCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                Toast.makeText(RetrofitActivity.this, "Upvote successful", Toast.LENGTH_LONG).show();
            } else {
                Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
                Toast.makeText(RetrofitActivity.this, "You already upvoted this answer", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    };

    /**
     * Okhttp part
     * 我们用一个简单的例子演示OkHttp的同步请求，首先通过HandlerThread创建一个异步线程，通过发送消息的形式,
     * 通知它发起网络请求，请求完毕之后，将结果中的body部分发送回主线程用TextView进行展示
     */
    private void initOkhttp() {
        mBtRequest = findViewById(R.id.request_sync);
        mBtRequestAsync = findViewById(R.id.request_async);
        mTvResult = findViewById(R.id.tv_result);
        mBtRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSyncRequest();
            }
        });

        mBtRequestAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsyncRequest();
            }
        });

        HandlerThread backgroundThread = new HandlerThread("backgroundThread");
        backgroundThread.start();
        mBackgroundHandler = new BackgroundHandler(backgroundThread.getLooper());//定义子线程Looper
        mMainHandler = new MainHandler();
    }

    //发起同步请求
    private void startSyncRequest() {
        //发送消息到异步线程，发起请求。
        mBackgroundHandler.sendEmptyMessage(MSG_REQUEST);
    }

    //发起异步请求
    private void startAsyncRequest() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(URL).build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                String result = response.body().string();
                //返回结果给主线程。
                Message message = mMainHandler.obtainMessage(MSG_UPDATE_UI, result);
                mMainHandler.sendMessage(message);
            }
        });
    }

    private class BackgroundHandler extends Handler {

        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //在异步线程发起请求。
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            okhttp3.Call call = client.newCall(request);
            try {
                okhttp3.Response response = call.execute();
                String result = response.body().string();
                Message message = mMainHandler.obtainMessage(MSG_UPDATE_UI, result);
                mMainHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //todo: 主线程获取到结果之后进行更新。
            mTvResult.setText((String) msg.obj);
        }
    }

}