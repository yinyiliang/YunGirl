package yyl.yungirl.ui.view;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface SwipeRefreshView extends BaseView {

    void showRefresh();

    void hideRefresh();

    void getDataFinish();

    void showEmpty();

    void showFailError(Throwable throwable);
}
