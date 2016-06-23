package yyl.yungirl.api;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import yyl.yungirl.widget.YunFactory;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class YunRetrofit {

    private YunApi gankService;

    private static YunRetrofit retrofit = null;

    private OkHttpClient mOkHttpClient;

    private OkHttpClient setupOkHttp () {
        if (mOkHttpClient == null) {
            synchronized (YunRetrofit.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(mLoggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();
                }
            }
        }
        return mOkHttpClient;
    }

    public synchronized static YunRetrofit getRetrofit(int hostType) {
        if (retrofit == null) {
            retrofit = new YunRetrofit(hostType);
        }
        return retrofit;
    }

    private YunRetrofit(int hostType) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YunFactory.getHost(hostType))
                .client(setupOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        gankService = retrofit.create(YunApi.class);

    }

    public YunApi getGankService() {
        return gankService;
    }

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request request = chain.request();
            final Response response = chain.proceed(request);

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    Logger.e("");
                    Logger.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                Logger.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
                Logger.json(buffer.clone().readString(charset));
                Logger.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }

            return response;
        }
    };
}
