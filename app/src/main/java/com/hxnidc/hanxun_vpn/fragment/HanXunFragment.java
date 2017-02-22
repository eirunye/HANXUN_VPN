package com.hxnidc.hanxun_vpn.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hxnidc.hanxun_vpn.R;
import com.hxnidc.hanxun_vpn.constant.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by on 2017/1/10 14:48
 * Author：yrg
 * Describe:
 */


public class HanXunFragment extends Fragment {


    @BindView(R.id.webView_HanXun)
    WebView mWebView;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    public static HanXunFragment newInstance() {
        Bundle args = new Bundle();
        HanXunFragment fragment = new HanXunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hanxun, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //设置webview支持javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar1.setProgress(newProgress);//设置进度值
                }
            }
        });
        mWebView.loadUrl(Constant.HANXUN_HTTP);
    }


}
