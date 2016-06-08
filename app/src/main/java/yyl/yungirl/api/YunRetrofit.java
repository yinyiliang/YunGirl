package yyl.yungirl.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class YunRetrofit {

    private GankApi gankService;

    public static final String HOST = "http://gank.io/api/";

    private static YunRetrofit retrofit = null;

    public synchronized static YunRetrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new YunRetrofit();
        }
        return retrofit;
    }

    private YunRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        gankService = retrofit.create(GankApi.class);

    }

    public GankApi getGankService() {
        return gankService;
    }
}
