package yyl.yungirl.ui.activity.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.PopupWindow;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.listener.HidingScrollListener;
import yyl.yungirl.presenter.DailyGankPresenter;
import yyl.yungirl.ui.activity.PictureActivity;
import yyl.yungirl.ui.activity.WebViewActivity;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.adpter.DailyGankAdapter;
import yyl.yungirl.ui.view.IDailyView;
import yyl.yungirl.util.CustomPopupWindow;
import yyl.yungirl.util.DateUtil;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by yinyiliang on 2016/8/10 0010.
 */
public class DailyGankFragment extends Fragment implements IDailyView, DailyGankAdapter.GankItemClickListener {

    @BindView(R.id.id_fab)
    FloatingActionButton fab;

    @BindView(R.id.daily_recycler_view)
    RecyclerView mRecyclerView;

    private DailyGankPresenter mPresenter;

    private DailyGankAdapter mAdapter;
    private List<Gank> mGankList;

    //日期相关
    private Date mCurrentDate;
    private List<String> mDateList;

    private CustomPopupWindow mPopupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily,container,false);
        ButterKnife.bind(this,view);
        initDatas();
        initViews();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initDatas(){
        mGankList = new ArrayList<>();
        mPresenter = new DailyGankPresenter(this);
        mCurrentDate = new Date(System.currentTimeMillis());

        mDateList = new ArrayList<>();
    }

    /**
     * 初始化主页面
     */
    private void initViews(){
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        final int fabBottomMargin = lp.bottomMargin;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DailyGankAdapter(getActivity(),mGankList);
        mAdapter.setItemClick(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void fabHide() {
                fab.animate()
                        .translationY(fab.getHeight() +fabBottomMargin )
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
            }

            @Override
            public void fabShow() {
                fab.animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
            }
        });

        //fab点击  刷新数据并回到数据顶部
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getDailyData(new Date(System.currentTimeMillis()));
                mRecyclerView.scrollToPosition(0);
            }
        });
    }

    /**
     * 从服务器加载数据
     */
    public void getData() {
        mPresenter.getDailyData(mCurrentDate);
        //Logger.t("现在时间").e(mCurrentDate+"");
        mDateList = mPresenter.getDateData();
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    /**
     * 菜单点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date:
                showDateListpopup();
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void flushData(List<Gank> data) {
        mAdapter.updateWithClear(data);
    }

    /**
     * 显示历史日期PopupWindow
     */
    @Override
    public void showDateListpopup() {
        mPopupWindow = new CustomPopupWindow(this,mDateList);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8
        getActivity().getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getActivity().getWindow().setAttributes(params);
            }
        });
        mPopupWindow.showAtLocation(LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_main, null), Gravity.CENTER, 0, 0);
    }

    /**
     * PopupWindow的点击反馈
     */
    public void dateFromPopWindow(String s) throws ParseException {
        Date date = DateUtil.parseToDate(s);
        mPresenter.getDailyData(date);
    }

    /**
     *
     */
    @Override
    public void showHint(View view, String s) {
        HintUtil.showSnackbar(view,s);
    }

    /**
     * 网络加载出错显示
     */
    @Override
    public void showNetError(Throwable throwable) {
        final Snackbar errorSnackbar = Snackbar.make(mRecyclerView, "无法加载数据，请检查网络是否连接，再点击重试！", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        errorSnackbar.show();
    }

    /**
     *  每日妹纸图点击事件
     */
    @Override
    public void onItemGirlClick(Gank gank, View position) {
        Intent intent = PictureActivity.intentPictureActivity((BaseActivity) getActivity(),
                gank.url, gank.desc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),
                    position, YunFactory.TRANSIT_PIC).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 每日干货点击事件
     */
    @Override
    public void onItemTitleClick(Gank gank, View position) {
        WebViewActivity.toWebViewActivity((BaseActivity) getActivity(),gank.url,gank.desc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
