package yyl.yungirl.ui.view;


/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface SwipeRefreshView extends BaseView {

    void requestDataRefresh();

    void setRefresh(boolean refresh);

    void showEmpty();

    void showNetError(Throwable throwable);
}
