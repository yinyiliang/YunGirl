package yyl.yungirl.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.orhanobut.logger.Logger;

import rx.Subscriber;
import yyl.yungirl.api.YunRetrofit;
import yyl.yungirl.data.bean.YunVersion;
import yyl.yungirl.widget.YunFactory;


/**
 * Created by yinyiliang on 2016/7/1 0001.
 */
public class CheckVersion {


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkVersion(final Context context, final View view, final String hint) {
        YunRetrofit.getRetrofit().fetchVersion()
                .subscribe(new Subscriber<YunVersion>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(YunVersion yunVersion) {
                        String firVersionName = yunVersion.versionShort;
                        String currentVersionName = getVersion(context);
                        if (currentVersionName != null) {
                            if (currentVersionName.compareTo(firVersionName) < 0) {
                                showUpdateDialog(yunVersion, context);
                            } else {
                                if (hint != null) {
                                    HintUtil.showSnackbar(view,hint);
                                }
                            }
                        }
                    }
                });
    }

    private static void showUpdateDialog(final YunVersion yunVersion, Context context) {
        String title = "发现新版" + yunVersion.name + "版本号：" + yunVersion.versionShort;

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(yunVersion.changelog)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YunFactory.useOtherBrowser(yunVersion.updateUrl);
                    }
                }).show();
    }

}
