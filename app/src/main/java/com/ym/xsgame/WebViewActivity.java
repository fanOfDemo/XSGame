package com.ym.xsgame;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String titleText, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleText = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
//        toolbar.setTitle(titleText);
        Log.e("WebActivity", titleText + " " + url);

        initView();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.md_activity_web_view;
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        // 允许执行javascript语句
        webSettings.setJavaScriptEnabled(false);
        // webSettings.setPluginState(WebSettings.PluginState.ON);
        // mWebView.addJavascriptInterface(new Handler(), "handler");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // webSettings.setBuiltInZoomControls(true);
        // webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.removeJavascriptInterface("searchBoxJavaBredge_");
        mWebView.loadUrl(url);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                    mProgressBar.postInvalidate();
                }
            }

            @Override
            public void onReceivedTitle(final WebView view, final String title) {
                if (title != null) {
                    titleText = title;
//                    toolbar.setTitle(fixString(titleText, 14, true));
                }
                super.onReceivedTitle(view, title);

            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 加载完成
                if (!TextUtils.isEmpty(titleText) &&
                        !TextUtils.isEmpty(mWebView.getTitle())) {
//                    toolbar.setTitle(fixString(titleText, 14, true));

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                mWebView.loadUrl(url);
                return true;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public String fixString(String src, int len, boolean isAddDel) {
        if (src.length() <= len)
            return src;
        return src.substring(0, len) + (isAddDel ? "..." : "");
    }

}
