package yyl.yungirl.presenter;

import android.app.Activity;

import yyl.yungirl.ui.view.BaseView;
import yyl.yungirl.ui.view.IMeizhiListView;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public BasePresenter(T mMvpView) {
        this.mMvpView = mMvpView;
    }
}
