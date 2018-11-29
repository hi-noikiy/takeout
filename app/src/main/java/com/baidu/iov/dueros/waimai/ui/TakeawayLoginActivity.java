package com.baidu.iov.dueros.waimai.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.KeyEvent;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.presenter.MeituanAuthPresenter;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;

import java.util.List;

//import com.baidu.iov.dueros.waimai.waimaiapplication.R;

public class TakeawayLoginActivity extends BaseActivity<MeituanAuthPresenter, MeituanAuthPresenter.MeituanLoginUi> implements
        MeituanAuthPresenter.MeituanLoginUi {

    private static final String TAG = TakeawayLoginActivity.class.getSimpleName();
    private WebView mWVMeituan;
    private ProgressBar progressBar;
    private MeituanAuthorizeReq mMeituanAuthReq;
    private AddressListReqBean mAddressListReq;
    private final String baiduUrl = "http://sandbox.codriverapi.baidu.com/";
    private final long SIX_HOUR = 6 * 60 * 60 * 1000;
    Bundle savedInstanceState;

    @Override
    MeituanAuthPresenter createPresenter() {
        return new MeituanAuthPresenter();
    }

    @Override
    MeituanAuthPresenter.MeituanLoginUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNoRequestForPermissions(true);
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        init();
        setContentView(R.layout.activity_meituan_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWVMeituan = (WebView) findViewById(R.id.meituan_login);

        mWVMeituan.addJavascriptInterface(this, "android");
        mWVMeituan.setWebViewClient(webViewClient);
        mWVMeituan.setWebChromeClient(webChromeClient);

        WebSettings webSettings = mWVMeituan.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWVMeituan.getSettings().setSupportZoom(true);
        mWVMeituan.getSettings().setBuiltInZoomControls(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CacheUtils.getBduss() == null || "".equals(CacheUtils.getBduss())) {
            getPresenter().requestAccountInfo();
        } else {
            getPresenter().requestMeituanAuth(mMeituanAuthReq);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void init() {
        mMeituanAuthReq = new MeituanAuthorizeReq();
        mMeituanAuthReq.setBduss(CacheUtils.getBduss());
        mAddressListReq = new AddressListReqBean();
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public void update(MeituanAuthorizeResponse data) {
        if (data.getIov().getAuthorizedState()) {
            if (CacheUtils.getAuth()) {
                //getPresenter().requestAddressListData(mAddressListReq);
                startIntent();
            } else {
                getPresenter().requestAuthInfo();
            }
        } else {
            syncCookie(this, baiduUrl);
            mWVMeituan.loadUrl(data.getIov().getAuthorizeUrl());
        }
    }

    private void syncCookie(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, Config.COOKIE_VALUE);
    }

    private void startIntent() {
        long time = CacheUtils.getAddrTime();
        if (time == 0 || (System.currentTimeMillis() - time > SIX_HOUR)) {
            getPresenter().requestAddressListData(mAddressListReq);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void failure(String msg) {
        finish();
    }

    @Override
    public void accountSuccess(String msg) {
        if (Constant.ACCOUNT_LOGIN_SUCCESS.equals(msg)) {
            Lg.getInstance().d(TAG, "account login success");
            getPresenter().requestMeituanAuth(mMeituanAuthReq);
        }
    }

    @Override
    public void accountFailure(String msg) {
        if (Constant.ACCOUNT_LOGIN_FAIL.equals(msg)) {
            Lg.getInstance().d(TAG, "account login fail");
            finish();
        }
    }

    @Override
    public void authSuccess(String msg) {
        if (Constant.ACCOUNT_AUTH_SUCCESS.equals(msg)) {
            Lg.getInstance().d(TAG, "account auth success");
            //getPresenter().requestAddressListData(mAddressListReq);
            startIntent();
        }
    }

    @Override
    public void authFailure(String msg) {
        if (Constant.ACCOUNT_AUTH_FAIL.equals(msg)) {
            Lg.getInstance().d(TAG, "account auth fail");
            finish();
        }
    }

    @Override
    public void getAddressListSuccess(List<AddressListBean.IovBean.DataBean> data) {
        Lg.getInstance().d(TAG, "get addresslist success");
        Intent addressIntent = new Intent(this, AddressSelectActivity.class);
        //if (data.size() == 0) {
            //addressIntent = new Intent(this, AddressEditActivity.class);
            //addressIntent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, false);
        //} else {
            //addressIntent = new Intent(this, AddressSelectActivity.class);
        //}
        startActivity(addressIntent);
        finish();
    }

    @Override
    public void getAddressListFail(String msg) {
        Lg.getInstance().d(TAG, "get addresslist fail");
        ToastUtils.show(this, msg,Toast.LENGTH_LONG);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWVMeituan.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWVMeituan.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWVMeituan.destroy();
        mWVMeituan = null;
    }
}
