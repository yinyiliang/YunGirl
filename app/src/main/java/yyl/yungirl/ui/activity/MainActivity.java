package yyl.yungirl.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yyl.yungirl.R;
import yyl.yungirl.adpter.DailyGankAdapter;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.presenter.DailyGankPresenter;
import yyl.yungirl.ui.view.IDailyView;
import yyl.yungirl.widget.YunFactory;

public class MainActivity extends SwipeRefreshActivity<DailyGankPresenter>
        implements IDailyView<Gank>,NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;

    private DailyGankPresenter mPresenter;

    private DailyGankAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Gank> mGankList;

    private Date mCurrentDate;

    private long exitTime = 0; ////记录第一次点击的时间

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefresh(true);
            }
        }, 358);
        getData();
    }

    /**
     * 初始化数据
     */
    private void initDatas(){
        mGankList = new ArrayList<>();
        mPresenter = new DailyGankPresenter(this);
        mCurrentDate = new Date(System.currentTimeMillis());
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
                getData();
                break;
            case  R.id.action_right:
                mCurrentDate = new Date(mCurrentDate.getTime() - YunFactory.ONE_DAY_TIME);
                getData();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            switch (id) {
                case R.id.nav_fuli:
                    startActivity(new Intent(MainActivity.this,MeizhiActivity.class));
                    break;
            }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        mCurrentDate = new Date(System.currentTimeMillis());
        getData();
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
     * 为空显示
     */
    @Override
    public void showEmpty() {
        Snackbar.make(mRecyclerView, "~~o(>_<)o编辑今天休息哦！", Snackbar.LENGTH_INDEFINITE).show();
    }

    /**
     * 网络加载错误显示
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showSnackbar(View view, String s) {

    }
}
