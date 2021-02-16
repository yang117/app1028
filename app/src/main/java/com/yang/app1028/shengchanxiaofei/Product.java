package com.yang.app1028.shengchanxiaofei;

import android.util.Log;

public class Product {
    private static final String TAG = Product.class.getSimpleName() ;
    private StringObject lock;

    public Product(StringObject lock) {
        this.lock = lock;
    }

    public void setValue() {
        synchronized (lock) {
            try {
                if (!lock.getValue().equals("")) {
                    lock.wait(); //有值，不生产
                }
                //没有值，生产
                String value = System.currentTimeMillis() + ", " + System.nanoTime();
                Log.d("test", "set value = " + value);
                lock.setValue(value);
                lock.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
