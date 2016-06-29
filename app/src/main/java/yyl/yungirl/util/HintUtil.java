package yyl.yungirl.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * 提示功能类
 * Created by yinyiliang on 2016/6/21 0021.
 */
public class HintUtil {

    public static Context mContext;

    public HintUtil() {
    }

    public static void register (Context context) {
        mContext = context.getApplicationContext();
    }

    private static void check() {
        if (mContext == null) {
            throw new NullPointerException(
                    "Must initial call ToastUtils.register(Context context) in your " +
                            "<? " +
                            "extends Application class>");
        }
    }

    public static void showSnackbar (View view, String s) {
        Snackbar.make(view,s,Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbar (View view, int id) {
        Snackbar.make(view,id,Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarlong (View view, String s) {
        Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
    }

    public static void showToast (String s) {
        check();
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void showToast (int id) {
        check();
        Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
    }
}
