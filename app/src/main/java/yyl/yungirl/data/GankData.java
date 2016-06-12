package yyl.yungirl.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yyl.yungirl.data.bean.Gank;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class GankData extends BaseData {

    public List<String> category;
    public Result results;

    public class Result {
        @SerializedName("Android") public List<Gank> androidList;
        @SerializedName("iOS") public List<Gank> iOSList;
        @SerializedName("福利") public List<Gank> 妹纸List;
        @SerializedName("拓展资源") public List<Gank> 拓展资源List;
        @SerializedName("瞎推荐") public List<Gank> 瞎推荐List;
        @SerializedName("App") public List<Gank> appList;
    }
}
