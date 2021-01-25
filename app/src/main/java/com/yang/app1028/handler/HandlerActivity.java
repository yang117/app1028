package com.yang.app1028.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.yang.app1028.R;

import java.lang.ref.WeakReference;

public class HandlerActivity extends AppCompatActivity {
    private static final String TAG = "HandlerActivity";

    private Handler mHandler0;
    private Handler mHandler1;
    private SafeHandler safeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        safeHandler = new SafeHandler(this);
        mHandler0 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "mHandler0 get msg at: " + Thread.currentThread().getName());
                Log.d(TAG, "mHandler0 get message: " + msg.arg1 + ", " + msg.arg2 + ", "
                        + msg.getData().getString("string"));
            }
        };

        mHandler0.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mHandler0 post first runnable at " + Thread.currentThread().getName());
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启子线程Handler方法1
//                Looper.prepare();
//                mHandler1 = new Handler();

                Message msg = Message.obtain();
                msg.arg1 = 1;
                msg.arg2 = 2;
                Bundle bundle = new Bundle();
                bundle.putString("string", "this is string");
                msg.setData(bundle);
                mHandler0.sendMessage(msg);
                Log.d(TAG, "mHandler0 send msg at: " + Thread.currentThread().getName());

                mHandler0.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mHandler0 post second runnable at " + Thread.currentThread().getName());
                    }
                });
            }
        }).start();

        //开启子线程Handler方法2
        HandlerThread handlerThread = new HandlerThread("worker thread");
        handlerThread.start();
        mHandler1 = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "mHandler1 get msg at: " + Thread.currentThread().getName());
            }
        };
        mHandler1.sendMessage(Message.obtain());
        Log.d(TAG, "mHandler1 send msg at: " + Thread.currentThread().getName());
    }

    //P53：防止内存泄漏，将Handler定义成静态内部类，在内部持有Activity弱引用，并及时移除所有信息
    private static class SafeHandler extends Handler {
        private WeakReference<HandlerActivity> ref;

        public SafeHandler(HandlerActivity activity) {
            this.ref = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HandlerActivity activity = ref.get();
            if (activity != null) {
//                activity.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        safeHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}