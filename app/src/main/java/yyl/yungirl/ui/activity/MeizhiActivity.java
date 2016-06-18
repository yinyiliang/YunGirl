package yyl.yungirl.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.adpter.MeizhiAdapter;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.presenter.MeizhiListPresenter;
import yyl.yungirl.ui.activity.base.SwipeRefreshActivity;
import yyl.yungirl.ui.view.IMeizhiListView;
import yyl.yungirl.widget.YunFactory;

public class MeizhiActivity extends SwipeRefreshActivity<MeizhiListPresenter>
        implements IMeizhiListView<Meizhi> ,MeizhiAdapter.OnItemClickListener {

    private GridLayoutManager layoutManager;

    private MeizhiListPresenter mPresenter;

    //是不是第一次到底
    private boolean isFirstTimeTouchBottom = true;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private MeizhiAdapter mAdapter;
    private List<Meizhi> mMeizhiList;

    private int mPage = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_meizhi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("福利", true);
        initData();
        initRecyclerView();

    }

    //初始化数据
    private void initData() {
        mPresenter = new MeizhiListPresenter(this);
        mMeizhiList = new ArrayList<>();
        mPresenter.attachView(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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

        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MeizhiAdapter(mMeizhiList, this);
        mRecyclerView.setAdapter(mAdapter);
        //给每个Item设置点击动画
        mAdapter.setOnItemClickListener(this);

        //设置加载更多时，不用等到数据到底了才加载，给用户一个良好的体验
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition()
                        >= mAdapter.getItemCount() - 4;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    if (!isFirstTimeTouchBottom) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPage += 1;
                        loadData();
                    } else {
                        isFirstTimeTouchBottom = false;
                    }
                }
            }
        });

    }

    /**
     * 加载数据
     */
    private void loadData() {
        mPresenter.loadData(mPage);
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    @Override
    public void refreshData(List<Meizhi> data) {
        mMeizhiList.clear();
        mMeizhiList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     *
     * @param data
     */
    @Override
    public void loadMoreData(List<Meizhi> data) {
        mMeizhiList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 重新加载数据
     */
    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        mPage = 1;
        loadData();
    }

    /**
     * 当数据没加载好时提示界面
     */
    @Override
    public void showEmpty() {
        showSnackbar(mRecyclerView,"妹子还在化妆~~o(>_<)o");
    }

    /**
     * 数据加载失败显示
     *
     * @param throwable
     */
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

    /**
     * 活动结束释放界面
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showSnackbar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_INDEFINITE).show();
    }

    /**
     * 妹纸图点击事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(final View view, int position) {
        Meizhi clickMeizhi = mAdapter.getMeizhi(position);
        Intent intent = PictureActivity.intentPictureActivity(MeizhiActivity.this,
                clickMeizhi.url, clickMeizhi.desc);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                this,view,YunFactory.TRANSIT_PIC).toBundle());
    }
}
