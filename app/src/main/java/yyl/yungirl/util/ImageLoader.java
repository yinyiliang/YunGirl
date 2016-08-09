package yyl.yungirl.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import yyl.yungirl.R;

/**
 *
 * Glide的封装类
 * Created by yinyiliang on 2016/6/28 0028.
 */
public class ImageLoader {

    //普通加载
    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
                .thumbnail(0.1f)
                .error(R.drawable.img_error)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }
    //图片居中显示加载
    public static void loadCenter(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
                .thumbnail(0.1f)
                .error(R.drawable.img_error)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(view);
    }
    //加载显示为圆形图片
    public static void loadRoundRect(Context context, String url,
                 BitmapTransformation transformation, ImageView view) {
        Glide.with(context).load(url)
                .crossFade()
                .transform(transformation)
                .into(view);
    }
}
