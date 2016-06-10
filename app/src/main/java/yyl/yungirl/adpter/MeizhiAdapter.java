package yyl.yungirl.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import yyl.yungirl.R;
import yyl.yungirl.data.bean.Meizhi;
import yyl.yungirl.widget.MyImageView;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.ViewHolder> {

    private List<Meizhi> mList;
    private Context mContext;

    public MeizhiAdapter(List<Meizhi> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public OnItemClickListener itemClickListener;

    //暴露一个方法给外界调用
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.meizhi_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Meizhi meizhi = mList.get(position);

        holder.meizhi = meizhi;

        Glide.with(mContext)
                .load(meizhi.url)
                .centerCrop()
                .into(holder.meizhiView);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyImageView meizhiView;
        Meizhi meizhi;

        public ViewHolder(View itemView) {
            super(itemView);
            meizhiView = (MyImageView) itemView.findViewById(R.id.iv_meizhi);
            meizhiView.setCustomSize(50,50);
            meizhiView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
