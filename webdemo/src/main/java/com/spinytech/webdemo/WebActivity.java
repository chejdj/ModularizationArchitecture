package com.spinytech.webdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.spinytech.macore.MaApplication;
import com.spinytech.macore.router.LocalRouter;
import com.spinytech.macore.router.RouterRequest;
import com.spinytech.macore.tools.Logger;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = "WebActivity";

    private static final String LOCAL_HTML = "file:///android_asset/page.html";

    private WebView mContentWv;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContentWv = (WebView) findViewById(R.id.web);
        mContentWv.setWebViewClient(new MyWebViewClient());
        mContentWv.getSettings().setBuiltInZoomControls(true);
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.getSettings().setSupportZoom(true);
        mContentWv.getSettings().setUseWideViewPort(true);
        mContentWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mContentWv.getSettings().setLoadWithOverviewMode(true);
        mContentWv.loadUrl(LOCAL_HTML);
    }

    public void dispatchAction(String url) {
        int index = url.indexOf("your_protocol://");
        if (index >= 0) {
            String command = url.substring(index + "your_protocol://".length());
            try {
                LocalRouter.getInstance(MaApplication.getMaApplication())
                        .route(this, new RouterRequest.Builder(this).url(command).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.i(TAG, "shouldOverrideUrlLoading: " + url);
            if (!TextUtils.isEmpty(url) && url.contains("your_protocol://")) {
                dispatchAction(url);
            } else {
                mContentWv.loadUrl(url);
            }
            return true;
        }
    }
}
