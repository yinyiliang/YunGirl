package yyl.yungirl.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import yyl.yungirl.App;
import yyl.yungirl.R;
import yyl.yungirl.TimeRemind.AlarmManagers;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.util.ImageLoader;
import yyl.yungirl.util.SystemUtil;

/**
 * Created by yinyiliang on 2016/6/26 0026.
 */
public class SettingFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener {

    private static final String CHANGE_THEME = "change_theme";
    private static final String CLEAR_CACHE = "clear_cache";

    private Preference mChangeTheme;
    private Preference mClearCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        mChangeTheme = findPreference(CHANGE_THEME);
        mClearCache = findPreference(CLEAR_CACHE);

        mClearCache.setSummary(SystemUtil.getAutoFileOrFilesSize(
                (App.mContext.getCacheDir() + "/YunCache"),
                (App.mContext.getCacheDir() + "/image_manager_disk_cache")));

        mChangeTheme.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mClearCache == preference) {
            SystemUtil.cleanInternalCache(App.mContext);
            ImageLoader.cleanMemory(App.mContext);
            mClearCache.setSummary(SystemUtil.getAutoFileOrFilesSize(
                    (App.mContext.getCacheDir() + "/YunCache"),
                    (App.mContext.getCacheDir() + "/image_manager_disk_cache")));
            HintUtil.showToast("缓存已清除");
        } else if (mChangeTheme == preference) {
            AlarmManagers.register(App.mContext);
            HintUtil.showToast("此功能还未完成");
        }
        return false;
    }

}
