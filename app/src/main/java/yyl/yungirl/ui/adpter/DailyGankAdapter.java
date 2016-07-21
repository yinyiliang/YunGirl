package yyl.yungirl.ui.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import yyl.yungirl.R;
import yyl.yungirl.data.bean.Gank;
import yyl.yungirl.util.DateUtil;
import yyl.yungirl.util.ImageLoader;
import yyl.yungirl.widget.MyImageView;
import yyl.yungirl.util.StringStyleUtils;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class DailyGankAdapter extends RecyclerView.Adapter<DailyGankAdapter.ViewHolderItem> {

    private List<Gank> mGankList;
    private Context mContext;
    private GankItemClickListener mItemClick;

    public DailyGankAdapter(Context context,List<Gank> mGankList) {
        this.mContext = context;
        this.mGankList = mGankList;
    }

    public interface GankItemClickListener{
        //点击妹子图触发事件
        void onItemGirlClick(Gank view, View position);
        //点击gank资源触发事件
        void onItemTitleClick(Gank view, View position);

    }

    public void setItemClick(GankItemClickListener mItemClick) {
        this.mItemClick = mItemClick;
    }

    /**
     * before add data , it will remove history data
     * @param data
     */
    public void updateWithClear(List<Gank> data) {
        mGankList.clear();
        update(data);
    }

    /**
     * add data append to history data*
     * @param data new data
     */
    public void update(List<Gank> data) {
        formatGankData(data);
        notifyDataSetChanged();
    }

    /**
     * 数据类型枚举
     */
    private enum EItemType{
        ITEM_TYPE_GIRL,
        ITEM_TYPE_NORMAL,
        ITEM_TYPE_CATEGORY
    }

    @Override
    public int getItemViewType(int position) {
        Gank gank = mGankList.get(position);
        if (gank.isGirl()) {
            return EItemType.ITEM_TYPE_GIRL.ordinal();
        } else if (gank.isHeader){
            return EItemType.ITEM_TYPE_CATEGORY.ordinal();
        } else {
            return EItemType.ITEM_TYPE_NORMAL.ordinal();
        }
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == EItemType.ITEM_TYPE_GIRL.ordinal()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_meizhi_item,parent,false);
            return new ViewHolderGirl(v);
        } else if (viewType == EItemType.ITEM_TYPE_CATEGORY.ordinal()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_heard_item,parent,false);
            return new ViewHolderCategory(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_title_item,parent,false);
            return new ViewHolderNormal(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
       holder.bindItem(mContext,mGankList.get(position));
    }

    @Override
    public int getItemCount() {
        return mGankList.size();
    }

    /**
     * 所有Item ViewHolder的父类
     */
    abstract static class ViewHolderItem extends RecyclerView.ViewHolder {
        public ViewHolderItem(View itemView) {
            super(itemView);
        }
        abstract void bindItem(Context context, Gank gank);
    }

    /**
     * 每个文章标题的ViewHolder
     */
     class ViewHolderNormal extends ViewHolderItem{

        LinearLayout mGankParent;
        TextView gankTitle;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            gankTitle = (TextView) itemView.findViewById(R.id.tv_title_item);
            mGankParent = (LinearLayout) itemView.findViewById(R.id.main_item_gank_parent);
        }

        @Override
        void bindItem(final Context context, final Gank gank) {
            gankTitle.setText(StringStyleUtils.getGankInfoSequence(context,gank));
            mGankParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onItemTitleClick(gank,v);
                }
            });
        }
    }

    /**
     * 每日妹子图的ViewHolder
     */
    class ViewHolderGirl extends ViewHolderItem{

        MyImageView girlPic;
        TextView tvTime;

        public ViewHolderGirl(View itemView) {
            super(itemView);
            girlPic = (MyImageView) itemView.findViewById(R.id.iv_meizhi_pic);
            tvTime = (TextView) itemView.findViewById(R.id.tv_update_time);
            girlPic.setCustomSize(200,180);
        }

        @Override
        void bindItem(Context context, final Gank gank) {
            tvTime.setText(DateUtil.toDate(gank.publishedAt));
            Logger.e(DateUtil.toDate(gank.publishedAt));

            ImageLoader.load(context, gank.url, girlPic);

            girlPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onItemGirlClick(gank,v);
                }
            });
        }
    }

    /**
     * 数据类型的ViewHolder
     */
    class ViewHolderCategory extends ViewHolderItem{

        TextView catrgory;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            catrgory = (TextView) itemView.findViewById(R.id.tv_heard_item);
        }

        @Override
        void bindItem(Context context, Gank gank) {
            catrgory.setText(gank.type);
        }
    }

    /**
     * 过滤数组，添加新的header类型到数组中
     * @param data
     */
    private void formatGankData(List<Gank> data) {
        //Insert headers into list of items.
        String lastHeader = "";
        for (int i = 0; i < data.size(); i++) {
            Gank gank = data.get(i);
            String header = gank.type;
            if (!gank.isGirl() && !TextUtils.equals(lastHeader, header)) {
                //如果不是妹子图，type类型是第一次出现。就添加这个type名字为一个header
                Gank gankHeader = gank.clone();
                lastHeader = header;
                gankHeader.isHeader = true;
                mGankList.add(gankHeader);
            }
            gank.isHeader = false;
            mGankList.add(gank);
        }
    }

}
