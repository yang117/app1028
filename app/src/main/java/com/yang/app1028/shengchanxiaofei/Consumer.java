package com.yang.app1028.shengchanxiaofei;

import android.util.Log;

public class Consumer {
    private static final String TAG = Consumer.class.getSimpleName();
    private StringObject lock;

    public Consumer(StringObject lock) {
        this.lock = lock;
    }

    public void getValue() {
        synchronized (lock) {
            try {
                if (lock.getValue().equals("")) {
                    lock.wait(); //没有值，不消费
                }
                //有值
                Log.d("test", "get value = " + lock.getValue());
                lock.setValue("");
                lock.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
