package yyl.yungirl.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.adpter.DailyGankAdapter;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.presenter.DailyGankPresenter;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.view.CustomPopupWindow;
import yyl.yungirl.ui.view.IDailyView;
import yyl.yungirl.util.DateUtil;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.widget.YunFactory;

public class MainActivity extends BaseActivity<DailyGankPresenter>
        implements IDailyView<Gank>,NavigationView.OnNavigationItemSelectedListener,
        DailyGankAdapter.GankItemClickListener {

    //抽屉
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    //标题栏
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private DailyGankPresenter mPresenter;
    private DailyGankAdapter mAdapter;

    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;
    private List<Gank> mGankList;

    //日期相关
    private Date mCurrentDate;
    private List<String> mDateList;

    private CustomPopupWindow mPopupWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initDatas();
        initViews();
        initDrawer();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getData();
    }

    /**
     * 初始化数据
     */
    private void initDatas(){
        mGankList = new ArrayList<>();
        mPresenter = new DailyGankPresenter(this);
        mCurrentDate = new Date(System.currentTimeMillis());

        mDateList = new ArrayList<>();
        mDateList = mPresenter.getDateData();
    }

    /**
     * 从服务器加载数据
     */
    public void getData() {
        mPresenter.getDailyData(mCurrentDate);
    }

    /**
     * 初始化主页面
     */
    private void initViews(){
        setTitle("每日资源",false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DailyGankAdapter(this,mGankList);
        mAdapter.setItemClick(this);
        mRecyclerView.setAdapter(mAdapter);

        setSupportActionBar(toolbar);


    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            assert drawer != null;
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    /**
     * 返回键回调
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_fuli:
                    startActivity(new Intent(MainActivity.this,MeizhiActivity.class));
                    break;
                case R.id.nav_one:
                    startActivity(new Intent(MainActivity.this,OneActivity.class));
                    break;
            }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * 刷新数据
     * @param data
     */
    @Override
    public void refreshData(List<Gank> data) {
        mAdapter.updateWithClear(data);
    }

    /**
     * 显示历史日期PopupWindow
     */
    @Override
    public void showDateListpopup() {

        mPopupWindow = new CustomPopupWindow(this,mDateList);
        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8
        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getWindow().setAttributes(params);
            }
        });
        mPopupWindow.showAtLocation(LayoutInflater.from(this).inflate(
                R.layout.activity_main, null), Gravity.CENTER, 0, 0);
    }

    /**
     * PopupWindow的点击反馈
     * @param s
     * @throws ParseException
     */
    public void dateFromPopWindow(String s) throws ParseException {
        Date date = DateUtil.parseToDate(s);
        mPresenter.getDailyData(date);
    }

    /**
     *  每日妹纸图点击事件
     * @param gank
     * @param position
     */
    @Override
    public void onItemGirlClick(Gank gank, View position) {
        Intent intent = PictureActivity.intentPictureActivity(MainActivity.this,
                gank.url, gank.desc);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                this,
                position, YunFactory.TRANSIT_PIC).toBundle());
    }

    /**
     * 每日干货点击事件
     * @param gank
     * @param position
     */
    @Override
    public void onItemTitleClick(Gank gank, View position) {
        WebViewActivity.toWebViewActivity(this,gank.url,gank.desc);
    }

    /**
     * 网络加载错误显示
     * @param throwable
     */
    @Override
    public void showNetError(Throwable throwable) {
        showHint(mRecyclerView,"无法加载数据，请检查网络是否连接。");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    /**
     * 显示提示
     * @param view
     * @param s
     */
    @Override
    public void showHint(View view, String s) {
        HintUtil.showSnackbar(view,s);
    }
}
