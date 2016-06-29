package yyl.yungirl.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import yyl.yungirl.R;
import yyl.yungirl.presenter.base.BasePresenter;
import yyl.yungirl.ui.view.PictureView;
import yyl.yungirl.util.RxGirl;

/**
 * Created by yinyiliang on 2016/6/17 0017.
 */
public class PicturePresenter extends BasePresenter<PictureView> {

    public PicturePresenter(PictureView mMvpView) {
        super(mMvpView);
    }

    @Override
    public void attachView(PictureView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 保存图片到手机图库
     * @param context
     * @param url
     * @param title
     */
    public void saveImageToGallery(final Context context, String url, String title) {
        RxGirl.saveImageAndGetPathObservable(context, url, title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveFail(e.getMessage());
                    }

                    @Override
                    public void onNext(Uri uri) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                        String msg = String.format(context.getString(R.string.picture_has_save_to), appDir.getAbsolutePath());
                        getMvpView().saveSuccess(msg);
                    }
        });
    }

    /**
     * 分享图片
     * @param context
     * @param url
     * @param title
     */
    public void shareIamge(final Context context, String url, final String title) {
        RxGirl.saveImageAndGetPathObservable(context, url, title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().shareFail(e.getMessage());
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/jpeg");
                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_meizhi_to)));
                    }
        });
    }
}
