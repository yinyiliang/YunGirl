package yyl.yungirl.ui.activity.base;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.presenter.Presenter;

/**
 * 基础Activity
 *
 * 在此类中进行一些Toolbar和菜单项的设置
 */
public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected abstract int getLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        if (mToolbar == null) {
            throw new NullPointerException("please add a Toolbar in your layout.");
        }
        setSupportActionBar(mToolbar);
    }

    /**
     * 设置菜单数的默认值
     * @return 值小于0，不显示菜单（由代码主动添加)
     */
    protected int getMenuRes(){
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getMenuRes()<0)return true;
        getMenuInflater().inflate(getMenuRes(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  给toolbar设置标题，以及是否设置标题栏左边是否带有返回图标、是否能点击
     * @param title
     * @param showHome
     */
    @NonNull
    public void setTitle(String title,boolean showHome) {
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
        getSupportActionBar().setDisplayShowHomeEnabled(showHome);
    }

}
