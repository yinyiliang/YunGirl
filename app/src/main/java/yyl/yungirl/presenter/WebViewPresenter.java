package yyl.yungirl.presenter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.orhanobut.logger.Logger;

import yyl.yungirl.presenter.base.BasePresenter;
import yyl.yungirl.ui.view.IWebView;

/**
 * Created by yinyiliang on 2016/6/16 0016.
 */
public class WebViewPresenter extends BasePresenter<IWebView> {

    public WebViewPresenter(IWebView mMvpView) {
        super(mMvpView);
    }

    @Override
    public void attachView(IWebView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 设置WebView
     * @param webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setupWebView(WebView webView) {

        WebSettings settings  = webView.getSettings();
        settings.setJavaScriptEnabled(true); //支持js
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setSupportZoom(true); //支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        settings.supportMultipleWindows(); //多窗口
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不启用缓存功能
        settings.setNeedInitialFocus(true); //当WebView调用requestFocus时为WebView设置节点
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        webView.requestFocusFromTouch(); //支持获取手势焦点，输入账户、密码或其他
        webView.setWebViewClient(new WebClient());
        webView.setWebChromeClient(new ChromeClient());

    }

    /**
     * 加载网页
     * @param webView
     * @param url
     */
    public void loadUrl(WebView webView,String url) {
        webView.loadUrl(url);
    }

    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getMvpView().showErrordescription(error.getDescription().toString());
            }
        }
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            NumberProgressBar progressBar = getMvpView().getProgressBar();
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }

        }

    }

}
