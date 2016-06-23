package yyl.yungirl.widget;

/**
 * Created by yinyiliang on 2016/6/6 0006.
 */
public class YunFactory {
    //妹子图请求个数
    public static final int meizhiSize = 10;
    //一天的时间
    public static final long ONE_DAY_TIME = 1000*60*60*24;
    //共享元素标识
    public static final String TRANSIT_PIC = "picture";

    public static final int GANK = 1;

    public static final int ONE = 2;

    public static final String GANK_HOST = "http://gank.io/api/";

    public static final String ONE_HOST = "http://rest.wufazhuce.com/OneForWeb/one/";

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        switch (hostType) {
            case GANK:
                return GANK_HOST;
            case ONE:
                return ONE_HOST;
        }
        return "";
    }

}
