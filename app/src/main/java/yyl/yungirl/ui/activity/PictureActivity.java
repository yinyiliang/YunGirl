package yyl.yungirl.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.presenter.PicturePresenter;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.view.PictureView;
import yyl.yungirl.util.ImageLoader;

/**
 * Created by yinyiliang on 2016/6/18 0018.
 */
public class PictureActivity extends BaseActivity<PicturePresenter> implements PictureView {

    private static final String IMAGE_URL = "image_url";
    private static final String IMAGE_TITLE = "image_title";

    @BindView(R.id.iv_activity_pic)
    ImageView mImageView;

    private String mUrl, mTitle;

    private PicturePresenter mPresenter;

    public static Intent intentPictureActivity(BaseActivity context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(IMAGE_URL, url);
        intent.putExtra(IMAGE_TITLE, desc);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();



        //设置点击事件，隐藏或者显示Toolbar，方便全屏显示妹子图
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowToolbar();
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        mUrl = getIntent().getStringExtra(IMAGE_URL);
        mTitle = getIntent().getStringExtra(IMAGE_TITLE);
        setTitle(mTitle,true);
        ImageLoader.load(this, mUrl, mImageView);
        mPresenter = new PicturePresenter(this);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_picture;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pic_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pic_save:
                mPresenter.saveImageToGallery(this,mUrl,mTitle);
                return true;
            case R.id.pic_share:
                mPresenter.shareIamge(this,mUrl,mTitle);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showHint(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void showNetError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 保存成功弹出保存至某文件
     * @param msg
     */
    @Override
    public void saveSuccess(String msg) {
        showHint(mImageView,msg);
    }

    /**
     * 保存失败
     * @param msg
     */
    @Override
    public void saveFail(String msg) {
        showHint(mImageView,msg);
    }

    /**
     * 分享失败
     * @param msg
     */
    @Override
    public void shareFail(String msg) {
        showHint(mImageView,msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
