package yyl.yungirl.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import yyl.yungirl.App;
import yyl.yungirl.R;
import yyl.yungirl.util.SystemUtil;

/**
 * Created by yinyiliang on 2016/6/27 0027.
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private final String INTRODUCTION = "introduction";
    private final String STAR = "Star";
    private final String ENJOY_PLAYING = "enjoy_playing";
    private final String GITHUB = "github";
    private final String EMAIL = "email";

    private Preference mIntroduction;
    private Preference mStar;
    private Preference mPlaying;
    private Preference mGitHub;
    private Preference mEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mIntroduction = findPreference(INTRODUCTION);
        mStar = findPreference(STAR);
        mPlaying = findPreference(ENJOY_PLAYING);
        mGitHub = findPreference(GITHUB);
        mEmail = findPreference(EMAIL);

        //设置点击事件
        mIntroduction.setOnPreferenceClickListener(this);
        mStar.setOnPreferenceClickListener(this);
        mPlaying.setOnPreferenceClickListener(this);
        mGitHub.setOnPreferenceClickListener(this);
        mEmail.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mIntroduction == preference) {
            new AlertDialog.Builder(getActivity()).setTitle("关于项目")
                    .setMessage(R.string.about_app).show();
        } else if (mStar == preference) {

            new AlertDialog.Builder(getActivity()).setTitle("点赞")
                    .setMessage("去项目地址给作者个Star，鼓励下作者୧(๑•̀⌄•́๑)૭✧")
                    .setNegativeButton("复制", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SystemUtil.copyToClipBoard(App.mContext,"https://github.com/yinyiliang/YunGirl","已经复制到剪切板啦~");
                        }
                    })
                    .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            useOtherBrowser(getString(R.string.app_xml));
                        }
                    }).show();

        } else if (mPlaying == preference) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("请作者喝一杯咖啡？(*^__^*)")
                    .setMessage("点击之后，作者支付宝账号将会复制到剪切板，你就可以使用支付宝转账给作者啦")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                    SystemUtil.copyToClipBoard(App.mContext,"497078141@qq.com","已经复制到剪切板啦~");
                              }}).show();
        } else if (mGitHub == preference) {
            useOtherBrowser(getString(R.string.author_github));
        } else if (mEmail == preference) {
            SystemUtil.copyToClipBoard(App.mContext,"13642948820@163.com","已经复制到剪切板啦~");
        }
        return false;
    }

    /**
     * 隐式Intent打开网页
     * @param s
     */
    private void useOtherBrowser(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }
}
