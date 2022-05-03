package com.yang.app1028.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * 定义一个与JS对象映射关系的Android类：AndroidtoJs
 */
public class AndroidtoJs {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        Log.d("WebView", "JS调用了Android的hello方法");
    }
}
