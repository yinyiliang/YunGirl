package yyl.yungirl.ui.view;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface LargeMapView extends BaseView {
    //保存成功
    void saveSuccess(String msg);
    //保存失败
    void saveFail(String msg);
    //分享成功
    void shareSuccess(String msg);
    //分享失败
    void shareFail(String msg);
}
