package yyl.yungirl.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.presenter.WebViewPresenter;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.view.IWebView;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.util.SystemUtil;

/**
 * Created by yinyiliang on 2016/6/16 0016.
 */
public class WebViewActivity extends BaseActivity<WebViewPresenter> implements IWebView {

    private static final String URL = "url";
    private static final String TITLE = "title";
    private String myUrl;
    private String myTitle;

    @BindView(R.id.id_progress) NumberProgressBar mProgressBar;
    @BindView(R.id.id_web_view) WebView mWebView;

    private WebViewPresenter mPresenter;

    /**
     * 暴露一个方法给外界调用
     * @param context
     * @param url 地址
     * @param title 标题
     */
    public static void toWebViewActivity(BaseActivity context,String url,String title) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(URL,url);
        intent.putExtra(TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPresenter = new WebViewPresenter(this);

        myUrl = getIntent().getStringExtra(URL);
        myTitle = getIntent().getStringExtra(TITLE);
        setTitle(myTitle,true);

        mPresenter.setupWebView(mWebView);
        mPresenter.loadUrl(mWebView,myUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.web_refresh:
                mWebView.reload();
                return true;
            case R.id.web_copy:
                String copyDone = "复制成功";
                SystemUtil.copyToClipBoard(this,mWebView.getUrl(),copyDone);
                return true;
            case R.id.web_useAnother:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(myUrl);
                intent.setData(uri);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    /**
     * 为了防止WebView泄漏
     * 所以在destroy之前，把WebView从parent中remove掉,同样也提前detach掉。
     */
    @Override
    protected void onDestroy() {
        if (mWebView != null){
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showErrordescription(String msg) {
        showHint(mWebView,msg);
    }

    @Override
    public NumberProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void showHint(View view, String s) {
        HintUtil.showSnackbarlong(view,s);
    }

    @Override
    public void showNetError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
