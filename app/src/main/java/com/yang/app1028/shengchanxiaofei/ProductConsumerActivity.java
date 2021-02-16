package com.yang.app1028.shengchanxiaofei;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProductConsumerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
        String a = "hello";
        StringBuilder b = new StringBuilder();
    }

    private void init() {
        StringObject lock = new StringObject("");
        Product product = new Product(lock);
        Consumer consumer = new Consumer(lock);
        ThreadProduct threadProduct = new ThreadProduct(product);
        ThreadConsumer threadConsumer = new ThreadConsumer(consumer);
        threadProduct.start();
        threadConsumer.start();
    }
}