package yyl.yungirl.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yyl.yungirl.R;
import yyl.yungirl.adpter.DailyGankAdapter;
import yyl.yungirl.adpter.DateListAdapter;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.presenter.DailyGankPresenter;
import yyl.yungirl.ui.view.CustomPopupWindow;
import yyl.yungirl.ui.view.IDailyView;
import yyl.yungirl.widget.YunFactory;

public class MainActivity extends BaseActivity<DailyGankPresenter>
        implements IDailyView<Gank>,NavigationView.OnNavigationItemSelectedListener {
    //抽屉
    private DrawerLayout drawer;
    //标题栏
    private Toolbar toolbar;

    private DailyGankPresenter mPresenter;
    private DailyGankAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Gank> mGankList;

    //日期相关
    private Date mCurrentDate;
    private List<String> mDateList;

    private PopupWindow mPopupWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void getData() {
        mPresenter.getDailyData(mCurrentDate);
    }

    /**
     * 初始化主页面
     */
    private void initViews(){
        setTitle("每日资源",false);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DailyGankAdapter(this,mGankList);
        mRecyclerView.setAdapter(mAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_left:
                mCurrentDate = new Date(mCurrentDate.getTime() + YunFactory.ONE_DAY_TIME);
                Log.e("时间",mCurrentDate.toString());
                getData();
                break;
            case  R.id.action_right:
                mCurrentDate = new Date(mCurrentDate.getTime() - YunFactory.ONE_DAY_TIME);
                Log.e("时间",mCurrentDate.toString());
                getData();
                break;
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
     * 显示历史日期列表界面
     */
    @Override
    public void showDateListpopup() {

        CustomPopupWindow mPopupWindow = new CustomPopupWindow(this,mDateList);
        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        mPopupWindow.showAtLocation(LayoutInflater.from(this).inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
    }


    /**
     * 为空显示
     */
    @Override
    public void showEmpty() {
        showSnackbar(mRecyclerView,"~~o(>_<)o编辑今天休息哦！");
    }

    /**
     * 网络加载错误显示
     * @param throwable
     */
    @Override
    public void showNetError(Throwable throwable) {
        showSnackbar(mRecyclerView,"无法加载数据，请检查网络是否连接。");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showSnackbar(View view, String s) {
        Snackbar.make(view,s,Snackbar.LENGTH_SHORT).show();
    }
}
