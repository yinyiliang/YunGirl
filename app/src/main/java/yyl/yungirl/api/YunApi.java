package yyl.yungirl.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import yyl.yungirl.data.GankData;
import yyl.yungirl.data.DateData;
import yyl.yungirl.data.bean.OneData;
import yyl.yungirl.widget.YunFactory;
import yyl.yungirl.data.MeizhiData;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public interface YunApi {

    //妹子图
    @GET("data/福利/" + YunFactory.meizhiSize + "/{page}")
    Observable<MeizhiData> getMeizhiData(
            @Path("page") int page);

    //每日干货
    @GET("day/{year}/{month}/{day}") Observable<GankData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);


    //gank.io历史干货发布日期
    @GET("day/history")
    Observable<DateData> getDateData();

    //ONE一个 的每日一张图和一句话资源
    @GET("http://rest.wufazhuce.com/OneForWeb/one/OneForWeb/one/getHp_N")
    Observable<OneData> getOne(@Query("strDate") String strDate, @Query("strRow") int strRow);
}
