package yyl.yungirl.presenter.base;

import yyl.yungirl.ui.view.base.BaseView;

/**
 * Created by yinyiliang on 2016/6/7 0007.
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
