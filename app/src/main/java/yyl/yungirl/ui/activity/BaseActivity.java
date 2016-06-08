package yyl.yungirl.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.IllegalFormatException;

import yyl.yungirl.R;
import yyl.yungirl.presenter.Presenter;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity {

    protected Toolbar mToolbar;

    protected abstract int getLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
    }

    private void initToolbar() {
        if (mToolbar == null) {
            throw new NullPointerException("please add a Toolbar in your layout.");
        }
        setSupportActionBar(mToolbar);
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
