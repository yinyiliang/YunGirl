package yyl.yungirl.data.bean;

import java.io.Serializable;
import java.util.List;

import yyl.yungirl.widget.DailyGankType;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class Gank extends Soul implements Cloneable,Serializable {

    public String _id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    public boolean isHeader;

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
