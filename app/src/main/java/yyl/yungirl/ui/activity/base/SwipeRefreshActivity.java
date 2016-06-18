package yyl.yungirl.ui.activity.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.presenter.BasePresenter;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.view.SwipeRefreshView;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public abstract class SwipeRefreshActivity<P extends BasePresenter> extends BaseActivity<P>
        implements SwipeRefreshView {

    private boolean mIsRequestDataRefresh = false;

    @BindView(R.id.id_swipe_refresh_layout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
                requestDataRefresh();
            }
        });
    }

    /**
     * 请求数据刷新
     */
    @Override
    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    //设置刷新效果
    public void setRefresh(boolean requestDataRefresh) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            // 防止刷新消失太快，让子弹飞一会儿.
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 1000);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }
}
