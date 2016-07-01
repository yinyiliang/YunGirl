package yyl.yungirl;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.pgyersdk.crash.PgyCrashManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import yyl.yungirl.util.HintUtil;

/**
 * Created by yinyiliang on 2016/6/17 0017.
 */
public class App extends Application {

    public static Context mContext;

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        App app = (App) context.getApplicationContext();
        return app.mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PgyCrashManager.register(this);

        mContext = this;
        //注册提示工具的context
        HintUtil.register(this);
        //检测内存是否泄漏
        mRefWatcher = LeakCanary.install(this);
        //只有调试模式下 才启用日志输出
        if(BuildConfig.DEBUG){
            Logger.init("Gank").hideThreadInfo().methodCount(2);
        }else{
            Logger.init("Gank").logLevel(LogLevel.NONE);
        }

    }

}
