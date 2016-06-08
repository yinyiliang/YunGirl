package yyl.yungirl.presenter;

import yyl.yungirl.ui.view.BaseView;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public interface Presenter<V extends BaseView> {

    void attachView(V mvpView);

    void detachView();

}
