package yyl.yungirl.api;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import yyl.yungirl.data.GankData;
import yyl.yungirl.data.DateData;
import yyl.yungirl.data.bean.OneData;
import yyl.yungirl.data.bean.YunVersion;
import yyl.yungirl.widget.YunFactory;
import yyl.yungirl.data.MeizhiData;

/**
 * Created by yinyiliang on 2016/6/6 0006.
 */
public interface YunApi {

    //妹子图
    @GET("data/福利/" + YunFactory.meizhiSize + "/{page}")
    Observable<MeizhiData> getMeizhiData(
            @Header("Cache-Control") String cacheControl,
            @Path("page") int page);

    //每日干货
    @GET("day/{year}/{month}/{day}") Observable<GankData> getGankData(
            @Header("Cache-Control") String cacheControl,
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);


    //gank.io历史干货发布日期
    @GET("day/history")
    Observable<DateData> getDateData(@Header("Cache-Control") String cacheControl);

    //ONE一个 的每日一张图和一句话资源
    @GET("http://rest.wufazhuce.com/OneForWeb/one/OneForWeb/one/getHp_N")
    Observable<OneData> getOne(
            @Header("Cache-Control") String cacheControl,
            @Query("strDate") String strDate,
            @Query("strRow") int strRow);

    @GET("http://api.fir.im/apps/latest/577b70e9f2fc424d93000052")
    Observable<YunVersion> getVersion(@Query("api_token") String api_token);

}
