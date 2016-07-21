package yyl.yungirl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import yyl.yungirl.R;

/**
 * 自定义水平进度条
 * Created by yinyiliang on 2016/7/13 0013.
 */
public class HorizontalProgressBar extends ProgressBar {

    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_REACH_COLOR = 0xffEA5455;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp

    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);

    private Paint mPaint = new Paint();
    private int mRealWidth;

    public HorizontalProgressBar(Context context) {
        this(context,null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);
    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBar);

        mUnReachColor = ta.getColor(
                R.styleable.HorizontalProgressBar_progress_unreach_color,mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(
                R.styleable.HorizontalProgressBar_progress_unreach_height,mUnReachHeight);

        mReachColor = ta.getColor(
                R.styleable.HorizontalProgressBar_progress_reach_color,mReachColor);
        mReachHeight = (int) ta.getDimension(
                R.styleable.HorizontalProgressBar_progress_reach_height,mReachHeight);

        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal,height);

        mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            //给定的是精确值
            result = size;
        } else {
            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(mReachHeight,mUnReachHeight);
            if (mode == MeasureSpec.AT_MOST) {
                //不能超过给定的值
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        boolean noNeedUnReach = false;
        //绘制reach Bar
        float radio = getProgress()*1.0f / getMax();
        float progressX = radio*mRealWidth;
        if (progressX > mRealWidth) {
            progressX = mRealWidth;
            noNeedUnReach = true;
        }

        if (progressX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,progressX,0,mPaint);
        }

        //绘制unreach bar
        if (!noNeedUnReach) {
            float start = progressX;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                getResources().getDisplayMetrics());
    }

}
