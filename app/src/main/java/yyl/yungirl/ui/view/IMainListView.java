package yyl.yungirl.ui.view;

import java.util.List;

import yyl.yungirl.data.bean.Soul;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface IMainListView<T extends Soul> extends SwipeRefreshView {

    //刷新数据
    void refreshData(List<T> data);
    //加载更多
    void loadMoreData(List<T> data);

}
