package yyl.yungirl.presenter.base;

import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by yinyiliang on 2016/6/7 0007.
 */
public interface Presenter<V extends BaseView> {

    void attachView(V mvpView);

    void detachView();

}
