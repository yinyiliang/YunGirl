package yyl.yungirl.listener;

import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView的滑动监听类
 * Created by yinyiliang on 2016/7/12 0012.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_MIN = 20;
    private boolean isVisible = true;
    private int scrolledDistance = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //当往下滑动，且滑动距离大于限定的最小距离并且fab可见时，  隐藏fab
        if (scrolledDistance > HIDE_MIN && isVisible) {
            fabHide();
            isVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_MIN && !isVisible) {
            //当往上滑动，切滑动距离大于限定的最小距离并且fab不可见， 显示fab
            fabShow();
            isVisible = true;
            scrolledDistance = 0;
        }
        //当fab可见以及往下滑动 或者 fab不可见以及往上滑动时，计算出滑动距离
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void fabHide();

    public abstract void fabShow();
}
