package yyl.yungirl.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yyl.yungirl.api.YunRetrofit;
import yyl.yungirl.data.MeizhiData;
import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.ui.view.IMeizhiListView;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class MeizhiListPresenter extends BasePresenter<IMeizhiListView> {

    private List<Meizhi> mMeizhiList = new ArrayList<>();

    public MeizhiListPresenter(IMeizhiListView mMvpView) {
        super(mMvpView);
    }

    @Override
    public void attachView(IMeizhiListView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     *  加载服务器数据
     * @param page
     */
    public void loadData(final int page) {
        YunRetrofit.getRetrofit(YunFactory.GANK).getGankService().getMeizhiData(page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<MeizhiData, List<Meizhi>>() {
                    @Override
                    public List<Meizhi> call(MeizhiData meizhiData) {
                        return meizhiData.results;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Meizhi>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showNetError(e);
                    }

                    @Override
                    public void onNext(List<Meizhi> meizhis) {
                       if (meizhis.isEmpty()) {
                           getMvpView().showEmpty();
                       }
                        if (page == 1) {
                            getMvpView().refreshData(meizhis);
                       } else {
                            getMvpView().loadMoreData(meizhis);
                        }
                        getMvpView().setRefresh(false);
                        mMeizhiList.addAll(meizhis);
                    }
                });
    }
}
