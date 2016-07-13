package yyl.yungirl.ui.view;


import yyl.yungirl.ui.view.base.BaseView;
import yyl.yungirl.widget.HorizontalProgressBar;

/**
 * Created by ${yinyiliang} on 2016/6/16 0016.
 */
public interface IWebView extends BaseView {

    void showErrordescription(String msg);

    HorizontalProgressBar getProgressBar();
}
