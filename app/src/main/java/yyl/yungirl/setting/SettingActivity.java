package yyl.yungirl.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import yyl.yungirl.R;
import yyl.yungirl.ui.activity.base.BaseActivity;

/**
 * Created by yinyiliang on 2016/6/27 0027.
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置",true);

        SettingFragment settingFragment = new SettingFragment();

        getFragmentManager().beginTransaction().replace(R.id.framelayout, settingFragment).commit();
    }
}
