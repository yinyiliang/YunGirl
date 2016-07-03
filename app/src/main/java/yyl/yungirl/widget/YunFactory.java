package yyl.yungirl.widget;

import android.content.Intent;
import android.net.Uri;

import yyl.yungirl.App;
import yyl.yungirl.util.SystemUtil;

/**
 * 工具类
 * Created by yinyiliang on 2016/6/6 0006.
 */
public class YunFactory {
    //妹子图请求个数
    public static final int meizhiSize = 10;
    //一天的时间
    public static final long ONE_DAY_TIME = 1000*60*60*24;
    //共享元素标识
    public static final String TRANSIT_PIC = "picture";

    //Gank的BaseUrl
    public static final String GANK_HOST = "http://gank.io/api/";

    //设缓存有效期为四周
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 28;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
    public static final String CACHE_CONTROL_NETWORK = "max-age=";


    /**
     * 缓存策略判断
     * @return
     */
    public static String getCacheControl() {
        return SystemUtil.isConnected(App.mContext) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }

    /**
     * 隐式Intent打开网页
     * @param s
     */
    public static void useOtherBrowser(String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mContext.startActivity(intent);
    }

}
