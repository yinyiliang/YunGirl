package yyl.yungirl.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import yyl.yungirl.R;
import yyl.yungirl.presenter.BasePresenter;
import yyl.yungirl.ui.view.SwipeRefreshView;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public abstract class SwipeRefreshActivity<P extends BasePresenter> extends BaseActivity<P>
        implements SwipeRefreshView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_refresh_layout);
        initSwipeRefreshLayout();
    }

    /**
     * 设置刷新的效果
     */
    private void initSwipeRefreshLayout() {
        //设置刷新进度条的颜色更替
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (prepareRefresh()) {
                    onRefreshStarted();
                } else {
                    hideRefresh();
                }
            }
        });
    }

    protected boolean prepareRefresh(){return true;}

    protected abstract void onRefreshStarted();

    @Override
    public void showRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @CheckResult
    protected boolean isRefreshing(){
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void hideRefresh() {
        // 防止刷新消失太快，让子弹飞一会儿
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        },1000);
    }

    @Override
    public void getDataFinish() {
        hideRefresh();
    }
}
