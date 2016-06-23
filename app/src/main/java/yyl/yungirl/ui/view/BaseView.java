package yyl.yungirl.ui.view;

import android.view.View;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface BaseView {
    //弹出提示
    void showHint(View view,String s);

    //显示网络出错
    void showNetError(Throwable throwable);
}
