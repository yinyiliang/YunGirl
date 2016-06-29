package yyl.yungirl.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Formatter;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;

import yyl.yungirl.App;

/**
 * Created by yinyiliang on 2016/6/16 0016.
 */
public class SystemUtil {

    /**
     * 复制到剪切板
     * @param context
     * @param text
     * @param success
     */
    public static void copyToClipBoard(Context context, String text, String success) {
        ClipData clipData = ClipData.newPlainText("gank", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            }
            else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("获取文件大小失败!");
        }
        return Formatter.formatFileSize(App.mContext,blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else {
            file.createNewFile();
            Logger.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            }
            else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir() + "/YunCache");
    }

    /**
     * 根据文件名删除该目录下的所有文件
     * @param filePath
     */
    private static void deleteFilesByDirectory(String filePath) {
        File directory = new File(filePath);
        if (directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
