package yyl.yungirl.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yinyiliang on 2016/6/18 0018.
 */
public class RxGirl {

    public static Observable<Uri> saveImageAndGetPathObservable(final Context context, final String url, final String title) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context).load(url).asBitmap().into(-1,-1).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = title.replace('/', '-') + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(file);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                context.sendBroadcast(scannerIntent);
                return Observable.just(uri);
            }
        }).subscribeOn(Schedulers.io());
    }
}
