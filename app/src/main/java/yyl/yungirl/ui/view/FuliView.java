package yyl.yungirl.ui.view;

import java.util.List;

import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by yinyiliang on 2016/8/10 0010.
 */
public interface FuliView extends BaseView{

    //刷新数据
    void refreshData(List<Meizhi> data);
    //加载更多
    void loadMoreData(List<Meizhi> data);
    //判断是否需要显示刷新
    void requestDataRefresh();
    //设置刷新
    void setRefresh(boolean refresh);
    //为空时显示
    void showEmpty();

}
