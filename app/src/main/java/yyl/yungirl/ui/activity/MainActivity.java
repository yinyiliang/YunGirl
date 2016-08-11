package yyl.yungirl.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.about.AboutActivity;
import yyl.yungirl.setting.SettingActivity;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.activity.fragment.DailyGankFragment;
import yyl.yungirl.ui.activity.fragment.FuliFragment;
import yyl.yungirl.ui.activity.fragment.OneFragment;
import yyl.yungirl.util.CheckVersion;
import yyl.yungirl.util.GlideCircleTransform;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.util.ImageLoader;
import yyl.yungirl.util.PermissionsChecker;
import yyl.yungirl.widget.YunFactory;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    //抽屉
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.content_main)
    CoordinatorLayout mCoordinatorLayout;

    private long exitTime = 0; ////记录第一次点击的时间

    private FragmentManager mFragmentManager;

    private DailyGankFragment mDailyFragment;
    private FuliFragment mFuliFragment;
    private OneFragment mOneFragment;

    private static final int FRAGMENT_DAILY = 0;
    private static final int FRAGMENT_FULI = 1;
    private static final int FRAGMENT_ONE = 2;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPermissionsChecker = new PermissionsChecker(this);
        initDrawer();
        mFragmentManager = getSupportFragmentManager(); //打开程序时，主界面显示每日资源
        showFragment(FRAGMENT_DAILY);
        mToolbar.setTitle("每日资源");
        CheckVersion.checkVersion(this,mCoordinatorLayout,null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            //使用Glide的Transformation来自定义加载圆形ImageView
            navigationView.post(new Runnable() {
                @Override
                public void run() {
                    final ImageView circleImage = (ImageView) findViewById(R.id.profile_image);
                    if (circleImage != null) {
                        ImageLoader.loadRoundRect(
                                navigationView.getContext(),
                                YunFactory.mUrl,
                                new GlideCircleTransform(navigationView.getContext()),
                                circleImage);
                    }
                }
            });

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
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                HintUtil.showSnackbar(mCoordinatorLayout,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_daily:
                    showFragment(FRAGMENT_DAILY);
                    break;
                case R.id.nav_fuli:
                    showFragment(FRAGMENT_FULI);
                    break;
                case R.id.nav_one:
                    showFragment(FRAGMENT_ONE);
                    break;
                case R.id.nav_setting:
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 显示Fragment
     */
    private void showFragment(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case FRAGMENT_DAILY:
                mToolbar.setTitle("每日资源");
                if (mDailyFragment == null) {
                    mDailyFragment = new DailyGankFragment();
                    ft.add(R.id.framelayout_content,mDailyFragment);
                } else {
                    ft.show(mDailyFragment);
                }
                break;
            case FRAGMENT_FULI:
                mToolbar.setTitle("福利");
                if (mFuliFragment == null) {
                    mFuliFragment = new FuliFragment();
                    ft.add(R.id.framelayout_content,mFuliFragment);
                } else {
                    ft.show(mFuliFragment);
                }
                break;
            case FRAGMENT_ONE:
                mToolbar.setTitle("ONE");
                if (mOneFragment == null) {
                    mOneFragment = new OneFragment();
                    ft.add(R.id.framelayout_content,mOneFragment);
                } else {
                    ft.show(mOneFragment);
                }
                break;
        }
        ft.commit();
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        if (mDailyFragment != null) ft.hide(mDailyFragment);
        if (mFuliFragment != null) ft.hide(mFuliFragment);
        if (mOneFragment != null) ft.hide(mOneFragment);
    }
}
