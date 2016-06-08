package yyl.yungirl.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import yyl.yungirl.data.GankData;
import yyl.yungirl.data.RestVideoData;
import yyl.yungirl.widget.YunFactory;
import yyl.yungirl.data.MeizhiData;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public interface GankApi {

    @GET("data/福利/" + YunFactory.meizhiSize + "/{page}")
    Observable<MeizhiData> getMeizhiData(
            @Path("page") int page);

    @GET("day/{year}/{month}/{day}") Observable<GankData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    @GET("/data/休息视频/" + YunFactory.meizhiSize + "/{page}")
    Observable<RestVideoData> getRestVideoData(@Path("page") int page);
}
