package yyl.yungirl.ui.view;

import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface PictureView extends BaseView {
    //保存图片
    void saveSuccess(String msg);
    //保存失败
    void saveFail(String msg);
    //分享图片
    void shareFail(String msg);

}