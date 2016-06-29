package yyl.yungirl.about;

import android.os.Bundle;
import android.support.annotation.Nullable;

import yyl.yungirl.R;
import yyl.yungirl.ui.activity.base.BaseActivity;

/**
 * Created by yinyiliang on 2016/6/27 0027.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("关于",true);

        getFragmentManager().beginTransaction().replace(R.id.framelayout, new AboutFragment()).commit();
    }
}
