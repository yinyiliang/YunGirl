package yyl.yungirl.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yyl.yungirl.R;
import yyl.yungirl.api.YunRetrofit;
import yyl.yungirl.adpter.MeizhiAdapter;
import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.data.MeizhiData;
import yyl.yungirl.presenter.MeizhiListPresenter;
import yyl.yungirl.ui.view.IMeizhiListView;

public class MeizhiActivity extends SwipeRefreshActivity<MeizhiListPresenter>
        implements IMeizhiListView<Meizhi> {

    private GridLayoutManager layoutManager;

    private MeizhiListPresenter mPresenter;

    private RecyclerView mRecyclerView;
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
        setTitle("福利",true);
        initData();
        initRecyclerView();
        loadData();

    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MeizhiAdapter(mMeizhiList,this);
        mRecyclerView.setAdapter(mAdapter);

        //设置加载更多时，不用等到数据到底了才加载，给用户一个良好的体验
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition()
                        >= mAdapter.getItemCount() - 4;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    //显示刷新控件
                    showRefresh();
                    loadData();
                }
            }
        });


        //给每个Item设置点击动画
        mAdapter.setItemClickListener(new MeizhiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int positon) {
                view.animate()
                        .translationZ(20F).setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.animate()
                                        .translationZ(1f)
                                        .setDuration(500)
                                        .start();
                            }
                        }).start();
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mPresenter.loadData(mPage);
        mPage++;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mMeizhiList = new ArrayList<>();

        mPresenter = new MeizhiListPresenter(this);

        mPresenter.attachView(this);
    }

    /**
     *  是否准备好刷新
     * @return
     */
    @Override @CheckResult
    protected boolean prepareRefresh() {
        if (mPage <= 2) {
            mPage = 1;
            if (!isRefreshing()) {
                showRefresh();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 刷新是否开始
     */
    @Override
    protected void onRefreshStarted() {
        mPage = 1;
        loadData();
    }


    /**
     *  刷新数据
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
     * @param data
     */
    @Override
    public void loadMoreData(List<Meizhi> data) {
        mMeizhiList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 当数据没加载好时提示界面
     */
    @Override
    public void showEmpty() {
        Snackbar.make(mRecyclerView,"妹子还在化妆~~o(>_<)o",Snackbar.LENGTH_INDEFINITE).show();
    }

    /**
     * 数据加载失败显示
     * @param throwable
     */
    @Override
    public void showFailError(Throwable throwable) {
        final Snackbar errorSnackbar = Snackbar.make(mRecyclerView,"无法加载数据，请检查网络是否连接，再点击重试！",
                Snackbar.LENGTH_INDEFINITE);
        errorSnackbar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorSnackbar.dismiss();
                onRefreshStarted();
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
}
