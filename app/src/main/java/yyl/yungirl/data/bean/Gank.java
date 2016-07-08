package yyl.yungirl.data.bean;

import java.io.Serializable;
import java.util.Date;

import yyl.yungirl.widget.DailyGankType;

/**
 * 每日数据
 * Created by Administrator on 2016/6/7 0007.
 */
public class Gank extends Soul implements Cloneable,Serializable {

    public String _id;
    public String createdAt;
    public String desc;
    public Date publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    //数据类型判断
    //是不是类型名
    public boolean isHeader;

    //是不是妹子图资源
    public boolean isGirl() {
        return type.equals(DailyGankType.福利.name());
    }

    @Override
    public Gank clone() {
        Gank gank = null;
        try {
            return (Gank) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return gank;
    }
}
