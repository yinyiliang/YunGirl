package yyl.yungirl.ui.view;

import com.daimajia.numberprogressbar.NumberProgressBar;

import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by ${yinyiliang} on 2016/6/16 0016.
 */
public interface IWebView extends BaseView {

    void showErrordescription(String msg);

    NumberProgressBar getProgressBar();
}
