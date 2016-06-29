package yyl.yungirl.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yyl.yungirl.R;
import yyl.yungirl.data.bean.OneData;
import yyl.yungirl.presenter.OnePresenter;
import yyl.yungirl.ui.activity.base.BaseActivity;
import yyl.yungirl.ui.view.IOneView;
import yyl.yungirl.util.DateUtil;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.util.ImageLoader;

/**
 * Created by yinyiliang on 2016/6/22 0022.
 */
public class OneActivity extends BaseActivity<OnePresenter> implements IOneView {

    private OnePresenter mPresenter;


    private Date mCurrentDate;
    private Date toDayDate;
    private int strRow = 1;

    @BindView(R.id.iv_one)
    ImageView imageOne;
    @BindView(R.id.tv_one_hp_title)
    TextView hpTitle;
    @BindView(R.id.tv_one_author)
    TextView hpAuthor;
    @BindView(R.id.tv_one_content)
    TextView oneContent;
    @BindView(R.id.tv_one_date)
    TextView oneDate;
    @BindView(R.id.one_main_content)
    LinearLayout layout;


    @Override
    protected int getLayout() {
        return R.layout.activity_one;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ONE",true);
        ButterKnife.bind(this);
        initDatas();

    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mPresenter = new OnePresenter(this);
        toDayDate = new Date(System.currentTimeMillis());
        mCurrentDate = toDayDate;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_one,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.one_left:
                getNextDayOne();
                return true;
            case R.id.one_right:
                getLastDayOne();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData () {
        mPresenter.loadOneData(mCurrentDate,strRow);
    }

    /**
     * 获取ONE一个前一天的数据
     */
    private void getLastDayOne () {
        mCurrentDate = DateUtil.getLastdayDate(mCurrentDate);
        //ONE一个的数据从2012-10-08开始更新，再往前就没有数据了
        try {
            Date createDate = DateUtil.parseToDate("2012-10-08");
            int x = mCurrentDate.compareTo(createDate);
            if (x < 0) {
                HintUtil.showToast(R.string.one_loadmore_hint);
                mCurrentDate = createDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getData();
    }

    /**
     * 获取ONE一个的后一天的数据
     */
    private void getNextDayOne () {
        mCurrentDate = DateUtil.getNextdayDate(mCurrentDate);
        //如果mCurrentDate获取到的日期为明天，则弹出提示，并把mCurrentDate重置赋值为今天的日期。
        int x = mCurrentDate.compareTo(toDayDate);
        if (x > 0) {
            HintUtil.showToast(R.string.one_refresh_hint);
            mCurrentDate = toDayDate;
        }
        getData();
    }

    @Override
    public void showHint(View view, String s) {
        HintUtil.showSnackbar(oneContent,s);
    }

    @Override
    public void showNetError(Throwable throwable) {
        final Snackbar errorSnackbar = Snackbar.make(layout, "无法加载数据，请检查网络是否连接，再点击重试！", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        errorSnackbar.show();
    }


    @Override
    public void setUpView(OneData oneData) {
        ImageLoader.load(this, oneData.hpEntity.strThumbnailUrl, imageOne);
        hpTitle.setText(oneData.hpEntity.strHpTitle);
        hpAuthor.setText(oneData.hpEntity.strAuthor);
        oneContent.setText(oneData.hpEntity.strContent);
        oneDate.setText(oneData.hpEntity.strMarketTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
