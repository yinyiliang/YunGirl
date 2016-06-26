package yyl.yungirl.ui.view;

import java.util.List;

import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.data.bean.Soul;

/**
 * Created by yinylinag on 2016/6/7 0007.
 */
public interface IMeizhiListView extends SwipeRefreshView {

    //刷新数据
    void refreshData(List<Meizhi> data);
    //加载更多
    void loadMoreData(List<Meizhi> data);
}
