package yyl.yungirl.ui.view.base;



/**
 * Created by yinyiliang on 2016/6/7 0007.
 */
public interface SwipeRefreshView extends BaseView {

    void requestDataRefresh();

    void setRefresh(boolean refresh);

    void showEmpty();

}
