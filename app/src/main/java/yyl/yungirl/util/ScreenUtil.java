package yyl.yungirl.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import yyl.yungirl.App;

/**
 * 屏幕工具类：实现获取屏幕相关参数
 *
 * @author xys
 */
public class ScreenUtil {

    /**
     * 获取屏幕相关参数
     *
     * @param context context
     * @return DisplayMetrics 屏幕宽高
     */
    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕density
     *
     * @param context context
     * @return density 屏幕density
     */
    public static float getDeviceDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 获得去掉状态栏和标题栏的屏幕截图
     */
    private static Bitmap takeScreen(Activity activity) {

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        int width = getScreenSize(App.mContext).widthPixels;
        int height = getScreenSize(App.mContext).heightPixels;

        //去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1,0,statusBarHeight,width,height-statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 保存截图
     */
    private static void savePic(Activity activity, Bitmap bitmap, String strFileName) {
        FileOutputStream fos;
        try {
            fos = activity.openFileOutput(strFileName,Context.MODE_PRIVATE);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90,fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void shareAct(Activity act, String fileName, String text) {

        Uri uri = null;

        try {
            FileInputStream input = act.openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), bitmap, null, null));
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        act.startActivity(Intent.createChooser(shareIntent, act.getTitle()));
    }

    public static void share(Activity act, String text) {
        String saveFileName = "share_pic.jpg";
        savePic(act, takeScreen(act), saveFileName);
        shareAct(act,saveFileName,text);
    }
}
