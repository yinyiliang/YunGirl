package yyl.yungirl.util;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import yyl.yungirl.R;
import yyl.yungirl.ui.adpter.DateListAdapter;
import yyl.yungirl.ui.activity.MainActivity;


/**
 * 自定义PopupWindow类
 * Created by yinyiliang on 2016/6/14 0014.
 */
public class CustomPopupWindow extends PopupWindow {

    private MainActivity mContext;
    private List<String> dateList;
    private DateListAdapter mAdapter;

    public CustomPopupWindow(Activity context, List<String> dateList) {
        this.mContext = (MainActivity) context;
        this.dateList = dateList;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 加载自定义布局文件，转化为组件
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_datelist, null);
        // 设置显示的view
        setContentView(view);

        //初始化控件
        ListView dateListView = (ListView) view.findViewById(R.id.lv_date);
        mAdapter = new DateListAdapter(mContext, dateList);
        dateListView.setAdapter(mAdapter);

        //设置Item点击事件
        dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.popup_tv_date);
                String dayDate = textView.getText().toString();
                try {
                    mContext.dateFromPopWindow(dayDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });

        // 设置弹出窗体的高
        DisplayMetrics metrices = ScreenUtil.getScreenSize(mContext);
        int width = metrices.widthPixels;
        setWidth(2 * width / 3);
        setHeight(2 * width / 3);
        // 设置弹出窗体可点击
        setFocusable(true);
        //设置背景 为一个圆角四边形
        float[] outerR = new float[]{50, 50, 50, 50, 50, 50, 50, 50};
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
        //指定填充颜色
        drawable.getPaint().setColor(Color.WHITE);
        // 指定填充模式
        drawable.getPaint().setStyle(Paint.Style.FILL);
        setBackgroundDrawable(drawable);

        this.setAnimationStyle(R.style.PopupWindow_anim_style);
    }

}
