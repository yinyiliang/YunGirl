package yyl.yungirl.presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yyl.yungirl.presenter.base.BasePresenter;
import yyl.yungirl.api.YunRetrofit;
import yyl.yungirl.data.GankData;
import yyl.yungirl.data.DateData;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.ui.view.IDailyView;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by yinyiliang on 2016/6/11 0011.
 */
public class DailyGankPresenter extends BasePresenter<IDailyView> {

    private List<String> mDateList = new ArrayList<>();

    //辅助值
    private Date mCurrentDate;

    private List<Gank> mGankList = new ArrayList<>();

    public DailyGankPresenter(IDailyView mMvpView) {
        super(mMvpView);
    }

    @Override
    public void attachView(IDailyView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 获得gank每日数据资源
     * @param date
     */
    public void getDailyData(final Date date) {
        mCurrentDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        YunRetrofit.getRetrofit().getYunService()
                .getGankData(YunFactory.getCacheControl(), year,month,day)
                .subscribeOn(Schedulers.io())
                .map(new Func1<GankData, GankData.Result>() {
                    @Override
                    public GankData.Result call(GankData gankData) {
                        return gankData.results;
                    }
                })
                .map(new Func1<GankData.Result, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankData.Result result) {
                        return addAllResults(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showNetError(e);
                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
                        if (ganks.isEmpty()) {
                            getDailyData(new Date(date.getTime() - YunFactory.ONE_DAY_TIME));
                        } else {
                            getMvpView().flushData(ganks);
                        }
                        mGankList.addAll(ganks);
                    }
                });
    }

    /**
     * 选择添加需要每日资源
     * @param results
     * @return
     */
    private List<Gank> addAllResults(GankData.Result results) {
        mGankList.clear();
        // make meizi data is in first position
        if (results.妹纸List != null) mGankList.addAll(0, results.妹纸List);
        if (results.androidList != null) mGankList.addAll(1,results.androidList);
        if (results.iOSList != null) mGankList.addAll(results.iOSList);
        if (results.appList != null) mGankList.addAll(results.appList);
        if (results.前端List != null) mGankList.addAll(results.前端List);
        if (results.拓展资源List != null) mGankList.addAll(results.拓展资源List);
        if (results.瞎推荐List != null) mGankList.addAll(results.瞎推荐List);
        return mGankList;
    }

    /**
     * 获得历史资源发布日期
     * @return 日期数组
     */
    public List<String> getDateData() {

        YunRetrofit.getRetrofit().getYunService()
                .getDateData(YunFactory.getCacheControl())
                .subscribeOn(Schedulers.io())
                .map(new Func1<DateData, List<String>>() {
                    @Override
                    public List<String> call(DateData dateData) {
                        return dateData.results;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        mDateList.addAll(strings);
                    }
                });
        return mDateList;
    }

}
