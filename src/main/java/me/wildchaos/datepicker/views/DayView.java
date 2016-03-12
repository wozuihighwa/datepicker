package me.wildchaos.datepicker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.wildchaos.datepicker.R;

/**
 * Created by 孙俊伟 on 2016/2/24.
 */
public class DayView extends RelativeLayout {

    private static final int LINE_COLOR_WHITE = 0;
    private static final int LINE_COLOR_BLACK = 1;

    private int day;
    private String state;

    private Paint mPaint;
    private TextView mState;
    private TextView mDay;

    public DayView(Context context) {
        super(context);
        init();
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int parentWidth = getWidth();
        mState = (TextView) findViewById(R.id.tv_state);
        mDay = (TextView) findViewById(R.id.tv_day);
        addState(parentWidth);
        addDay(parentWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        canvas.drawLine(width / 2, 0, width, height / 2, mPaint);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
    }

    private void addState(int parentWidth) {
        RelativeLayout.LayoutParams lp = (LayoutParams) mState.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.rightMargin = parentWidth / 20;
        lp.topMargin = parentWidth / 20;
        mState.setLayoutParams(lp);
        mState.setTextSize(parentWidth / 18);
    }

    private void addDay(int parentWidth) {
        RelativeLayout.LayoutParams lp = (LayoutParams) mDay.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mDay.setLayoutParams(lp);
        mDay.setTextSize(parentWidth / 8);
        mState.setTextColor(ContextCompat.getColor(getContext(), R.color.DayGreyColor));
    }

    /**
     * 设置斜线的颜色
     *
     * @param resId
     */
    public void setObliqueLineColor(int resId) {
        mPaint.setColor(ContextCompat.getColor(getContext(), resId));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        invalidate();
    }

    /**
     * 设置背景色
     *
     * @param resId
     */
    public void setBgColor(final int resId) {
        post(new Runnable() {
            @Override
            public void run() {
                mDay.setBackgroundColor(ContextCompat.getColor(getContext(), resId));
            }
        });
    }

    /**
     * 设置 day 颜色
     *
     * @param resId
     */
    public void setDayColor(final int resId) {
        post(new Runnable() {
            @Override
            public void run() {
                mDay.setTextColor(ContextCompat.getColor(getContext(), resId));
            }
        });
    }

    /**
     * 获取 item 天
     */
    public int getItemDay() {
        return Integer.valueOf(mDay.getText().toString());
    }

    /**
     * 获取 item 状态
     */
    public String getItemState() {
        return mState.getText().toString();
    }
}
