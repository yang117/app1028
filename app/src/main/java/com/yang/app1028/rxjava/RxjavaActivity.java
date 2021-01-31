package com.yang.app1028.rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.yang.app1028.R;
import com.yang.app1028.retrofit.youdao.GetRequestService;
import com.yang.app1028.retrofit.youdao.Translation1;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxjavaActivity extends AppCompatActivity {
    private static final String TAG = RxjavaActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        initBasicObservable();
//        initExample();
    }

    private void initBasicObservable() {
        String[] words = {"A", "B", "C", "hello", "world"};
//        Observable<String> observable = Observable.fromArray(words);
//        Observable<String[]> observable = Observable.just(words);
//        Observable<String> observable = Observable.just("1", "3", "5", "7");

        //被观察者：顾客
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 2. 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 注：建议发送事件前检查观察者的isUnsubscribed状态，以便在没有观察者时，让Observable停止发射数据
//                if (!observer.isUnsubscribed()) {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {//观察者：厨房
                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "对Next事件" + value + "作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    private void initExample() {
        /**
         * 结合Retrofit+Rxjava2的例子
         */
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 步骤5：创建 网络请求接口 的实例
        GetRequestService service = retrofit.create(GetRequestService.class);
        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<Translation1> observable = service.postRxCall("I love you");

        // 步骤7：发送网络请求
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation1>() {
                    @Override
                    public void accept(Translation1 translation) throws Exception {
                        Log.d(TAG, "accept(): " + translation.getTranslateResult().get(0).get(0).getTgt());
                    }
                });
    }
}