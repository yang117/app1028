package com.yang.app1028.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yang.app1028.R;

import java.util.HashMap;
import java.util.Set;

public class WebViewSampleActivity extends AppCompatActivity {
    final int version = Build.VERSION.SDK_INT;

    private WebView mWebview;
    private WebSettings mWebSettings;
    private TextView mTitle, mBeginLoading, mProgress, mEndLoading;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mWebview = findViewById(R.id.webview);
        mTitle = findViewById(R.id.text_title);
        mBeginLoading = findViewById(R.id.text_beginLoading);
        mProgress = findViewById(R.id.text_progress);
        mEndLoading = findViewById(R.id.text_endLoading);
        mButton = findViewById(R.id.button);

        mWebSettings = mWebview.getSettings();
        initWebView4();
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

    //android call js
    private void initWebView2() {
        // 设置与Js交互的权限
        mWebSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebview.loadUrl("file:///android_asset/javascript.html");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过Handler发送消息
                mWebview.post(new Runnable() {
                    @Override
                    public void run() {
                        if (version < 18) {
                            // 调用javascript的callJS()方法.注意调用的JS方法名要对应上!
                            mWebview.loadUrl("javascript:callJS()");
                        } else {
                            //该方法比loadUrl()效率更高、使用更简洁。
                            //因为该方法的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会。Android 4.4才能用
                            mWebview.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    //此处为 js 返回的结果
                                }
                            });
                        }
                    }
                });
            }
        });

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        //设置响应js 的Alert()函数
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebViewSampleActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }

    //js call android through对象映射
    private void initWebView3() {
        // 设置与Js交互的权限
        mWebSettings.setJavaScriptEnabled(true);

        //通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        mWebview.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象

        mWebview.loadUrl("file:///android_asset/javascript.html");
    }

    private void initWebView4() {
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.loadUrl("file:///android_asset/javascript.html");

        // 复写WebViewClient类的shouldOverrideUrlLoading方法
        mWebview.setWebViewClient(new WebViewClient() {
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                  // 步骤2：根据协议的参数，判断是否是所需要的url
                  // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                  //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                  Uri uri = Uri.parse(url);
                  // 如果url的协议 = 预先约定的 js 协议
                  // 就解析往下解析参数
                  if (uri.getScheme().equals("js")) {

                      // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                      // 所以拦截url,下面JS开始调用Android需要的方法
                      if (uri.getAuthority().equals("webview")) {

                          //  步骤3：
                          // 执行JS所需要调用的逻辑
                          System.out.println("js调用了Android的方法");
                          // 可以在协议上带有参数并传递到Android上
                          HashMap<String, String> params = new HashMap<>();
                          Set<String> collection = uri.getQueryParameterNames();
                      }
                      return true;
                  }
                  return super.shouldOverrideUrlLoading(view, url);
              }
          }
        );
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