package com.yang.app1028.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yang.app1028.R;

public class WebViewSampleActivity extends AppCompatActivity {
    private WebView mWebview;
    private WebSettings mWebSettings;
    private TextView mTitle, mBeginLoading, mProgress, mEndLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mWebview = findViewById(R.id.webview);
        mTitle = findViewById(R.id.text_title);
        mBeginLoading = findViewById(R.id.text_beginLoading);
        mProgress = findViewById(R.id.text_progress);
        mEndLoading = findViewById(R.id.text_endLoading);

        initWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView() {
        mWebSettings = mWebview.getSettings();
        mWebview.loadUrl("http://www.baidu.com/");

        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mBeginLoading.setText("Begin Loading.");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mEndLoading.setText("End Loading.");
            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgress.setText(newProgress + " %");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}