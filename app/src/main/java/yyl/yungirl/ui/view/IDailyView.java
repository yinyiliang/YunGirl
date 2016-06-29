package yyl.yungirl.ui.view;


import java.util.List;


import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by yinyiliang on 2016/6/10 0010.
 */
public interface IDailyView extends BaseView {

    //刷新数据
    void flushData(List<Gank> data);
    //显示发过干货日期的对话框
    void showDateListpopup();

}
