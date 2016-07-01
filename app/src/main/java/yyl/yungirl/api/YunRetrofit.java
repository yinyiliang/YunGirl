package yyl.yungirl.api;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yyl.yungirl.App;
import yyl.yungirl.util.HintUtil;
import yyl.yungirl.util.SystemUtil;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by yinyiliang on 2016/6/6 0006.
 */
public class YunRetrofit {

    private YunApi yunService;

    private static YunRetrofit retrofit = null;

    private OkHttpClient mOkHttpClient;

    public synchronized static YunRetrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new YunRetrofit();
        }
        return retrofit;
    }

    private YunRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YunFactory.GANK_HOST)
                .client(setupOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        yunService = retrofit.create(YunApi.class);
    }

    public YunApi getGankService() {
        return yunService;
    }

    /**
     *  设置OkHttp
     * @return
     */
    private OkHttpClient setupOkHttp () {
        if (mOkHttpClient == null) {
            synchronized (YunRetrofit.class) {
                if (mOkHttpClient == null) {

                    //指定缓存路径、缓存大小50M
                    Cache cache = new Cache(new File(App.mContext.getCacheDir(), "YunCache"),
                            1024 * 1024 * 50);

                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(mCacheControlInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 设置缓冲拦截头
     */
    private Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!SystemUtil.isConnected(App.mContext)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                HintUtil.showToast("暂无网络");
            }

            Response response = chain.proceed(request);
            if (SystemUtil.isConnected(App.mContext)) {
                //有网的时候读接口上的@Headers里的配置
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma") //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                return response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + YunFactory.CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

//    // 打印返回的json数据拦截器
//    private Interceptor mLoggingInterceptor = new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//
//            final Request request = chain.request();
//            final Response response = chain.proceed(request);
//
//            final ResponseBody responseBody = response.body();
//            final long contentLength = responseBody.contentLength();
//
//            BufferedSource source = responseBody.source();
//            source.request(Long.MAX_VALUE); // Buffer the entire body.
//            Buffer buffer = source.buffer();
//
//            Charset charset = Charset.forName("UTF-8");
//            MediaType contentType = responseBody.contentType();
//            if (contentType != null) {
//                try {
//                    charset = contentType.charset(charset);
//                } catch (UnsupportedCharsetException e) {
//                    Logger.e("");
//                    Logger.e("Couldn't decode the response body; charset is likely malformed.");
//                    return response;
//                }
//            }
//
//            if (contentLength != 0) {
//                Logger.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
//                Logger.json(buffer.clone().readString(charset));
//                Logger.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
//            }
//
//            return response;
//        }
//    };
}
