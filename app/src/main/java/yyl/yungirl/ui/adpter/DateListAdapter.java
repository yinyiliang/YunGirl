package yyl.yungirl.ui.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yyl.yungirl.R;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class DateListAdapter extends BaseAdapter {

    private List<String> mDateList = new ArrayList<>();
    private LayoutInflater mInflater;

    public DateListAdapter(Context context,List<String> mDateList) {
        mInflater = LayoutInflater.from(context);
        this.mDateList = mDateList;
    }

    @Override
    public int getCount() {
        return mDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderDate holderDate = null;
        if (convertView == null) {
            holderDate = new ViewHolderDate();
            convertView = mInflater.inflate(R.layout.popup_datelist_item,null);
            holderDate.date = (TextView) convertView.findViewById(R.id.popup_tv_date);
            convertView.setTag(holderDate);
        } else {
            holderDate = (ViewHolderDate) convertView.getTag();
        }
        holderDate.date.setText(mDateList.get(position));
        return convertView;
    }

    public final class ViewHolderDate {
        public TextView date;
    }

}
