package yyl.yungirl.presenter;


import java.util.Date;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yyl.yungirl.api.YunRetrofit;
import yyl.yungirl.data.bean.OneData;
import yyl.yungirl.presenter.base.BasePresenter;
import yyl.yungirl.ui.view.IOneView;
import yyl.yungirl.util.DateUtil;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by yinyiliang on 2016/6/22 0022.
 */
public class OnePresenter extends BasePresenter<IOneView> {

    public OnePresenter(IOneView mMvpView) {
        super(mMvpView);
    }

    @Override
    public void attachView(IOneView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 加载ONE一个数据
     * @param date
     * @param strRow
     */
    public void loadOneData (final Date date, final int strRow) {

        final String strDate = DateUtil.toOneData(date);
        YunRetrofit.getRetrofit().getYunService()
                .getOne(YunFactory.getCacheControl(), strDate, strRow)
                .subscribeOn(Schedulers.io())
                .filter(new Func1<OneData, Boolean>() {
                    @Override
                    public Boolean call(OneData oneData) {
                        return oneData.result.equals("SUCCESS");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OneData>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showNetError(e);
                    }

                    @Override
                    public void onNext(OneData oneData) {
                        getMvpView().setUpView(oneData);
                    }
                });

    }
}

