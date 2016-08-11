package yyl.yungirl.ui.activity.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import yyl.yungirl.R;
import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.listener.CustomRecyclerOnScrollListener;
import yyl.yungirl.presenter.MeizhiListPresenter;
import yyl.yungirl.ui.activity.PictureActivity;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.adpter.MeizhiAdapter;
import yyl.yungirl.ui.view.FuliView;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by yinyiliang on 2016/8/10 0010.
 */
public class FuliFragment extends Fragment implements FuliView,
        MeizhiAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.id_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MeizhiListPresenter mPresenter;

    private MeizhiAdapter mAdapter;

    private List<Meizhi> mMeizhiList;

    private GridLayoutManager layoutManager;
    //默认加载第一页
    private int mPage = 1;

    //是不是第一次到底
    private boolean isFirstTimeTouchBottom = true;

    private boolean mIsRequestDataRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meizhi,container,false);
        ButterKnife.bind(this,view);
        initData();
        initSwipeRefreshLayout();
        initRecyclerView();
        return view;
    }

    /**
     * 设置刷新的效果
     */
    private void initSwipeRefreshLayout() {
        //设置刷新进度条的颜色更替
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    //初始化数据
    private void initData() {
        mPresenter = new MeizhiListPresenter(this);
        mMeizhiList = new ArrayList<>();
        mPresenter.attachView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefresh(true);
            }
        }, 358);
        loadData();
    }

    //初始化RecyclerView，以及给每个Item添加点击事件、滑动设置
    private void initRecyclerView() {

        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MeizhiAdapter(mMeizhiList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        //给每个Item设置点击动画
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.addOnScrollListener(new CustomRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                LoadMoreData();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

}

    private void LoadMoreData() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        mPage += 1;
                        loadData();
                        return null;
                    }
                }).subscribe();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mPresenter.loadData(mPage);
    }

    @Override
    public void refreshData(List<Meizhi> data) {
        mMeizhiList.clear();
        mMeizhiList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<Meizhi> data) {
        mMeizhiList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestDataRefresh() {
        mPage = 1;
        loadData();
    }

    @Override
    public void setRefresh(boolean refresh) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        if (!refresh) {
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

    @Override
    public void showEmpty() {
        showHint(mRecyclerView,"妹子还在化妆~~o(>_<)o");
    }

    @Override
    public void showHint(View view, String s) {
        HintUtil.showSnackbar(view,s);
    }

    @Override
    public void showNetError(Throwable throwable) {
        final Snackbar errorSnackbar = Snackbar.make(mRecyclerView, "无法加载数据，请检查网络是否连接，再点击重试！", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDataRefresh();
            }
        });
        errorSnackbar.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Meizhi clickMeizhi = mAdapter.getMeizhi(position);
        Intent intent = PictureActivity.intentPictureActivity((BaseActivity) getActivity(),
                clickMeizhi.url, clickMeizhi.desc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),view, YunFactory.TRANSIT_PIC).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 活动结束释放界面
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRefresh() {
        requestDataRefresh();
    }
}
