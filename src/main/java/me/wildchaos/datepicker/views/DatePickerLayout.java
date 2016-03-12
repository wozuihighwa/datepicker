package me.wildchaos.datepicker.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.wildchaos.datepicker.Config;
import me.wildchaos.datepicker.R;
import me.wildchaos.datepicker.model.DateItem;
import me.wildchaos.datepicker.util.DateUtil;

/**
 * Created by 孙俊伟 on 2016/2/25.
 */
public class DatePickerLayout extends LinearLayout {

    private static final int DAYS = 7;

    private LinearLayout monthSelectBar;
    private RelativeLayout btnPriorMonth;
    private RelativeLayout btnNextMonth;
    private ImageView imgPriorArrow;
    private ImageView imgNextArrow;
    private TextView selectedYear;
    private TextView selectedMonth;
    private LinearLayout weekNumBar;
    private DatePickerTable datePickerTable;

    private int mBgColor;
    private float mMonthSelectBarHeight;
    private float mWeekNumBarHeight;
    private float mSeletedYearMonthTextSize;
    private int mSeletedYearMonthTextColor;
    private float mWeekNumberTextSize;
    private float mMarginPaddingLeft;
    private float mMarginPaddingRight;

    private int mYear;              // 显示的年份
    private int mMonth;             // 显示的月份
    private int firstDayOfMonth;    // 一个月的第一天

    private String[] dayItemNames = new String[]{
            "日", "一", "二", "三", "四", "五", "六"
    };

    private DatePickerPriorNextListener listener;

    private MonthChangeListener monthChangeListener;

    public DatePickerLayout(Context context) {
        super(context);

    }

    public DatePickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DatePickerLayout);
        mBgColor = ta.getColor(R.styleable.DatePickerLayout_datepicker_background,
                ContextCompat.getColor(context, R.color.colorWhite));
        mMonthSelectBarHeight = ta.getDimension(R.styleable.DatePickerLayout_month_select_bar_height,
                getResources().getDimension(R.dimen.NavigetorHeight));
        mWeekNumBarHeight = ta.getDimension(R.styleable.DatePickerLayout_week_num_bar_height,
                getResources().getDimension(R.dimen.NavigetorHeight));
        mSeletedYearMonthTextSize = ta.getDimension(R.styleable.DatePickerLayout_seleted_year_month_text_size,
                getResources().getDimension(R.dimen.NavigetorTextSize));
        mSeletedYearMonthTextColor = ta.getColor(R.styleable.DatePickerLayout_seleted_year_month_text_color,
                ContextCompat.getColor(context, R.color.ToolBarColor));
        mWeekNumberTextSize = ta.getDimension(R.styleable.DatePickerLayout_week_number_text_size,
                getResources().getDimension(R.dimen.WeekNumberTextSize));
        mMarginPaddingLeft = ta.getDimension(R.styleable.DatePickerLayout_margin_padding_left,
                getResources().getDimension(R.dimen.WeekBarMargin));
        mMarginPaddingRight = ta.getDimension(R.styleable.DatePickerLayout_margin_padding_right,
                getResources().getDimension(R.dimen.WeekBarMargin));

        ta.recycle();

        // 设置垂直
        setOrientation(LinearLayout.VERTICAL);
        // 设置背景色
        setBackgroundColor(mBgColor);

        monthSelectBar = new LinearLayout(context);
        btnPriorMonth = new RelativeLayout(context);
        imgPriorArrow = new ImageView(context);
        selectedYear = new TextView(context);
        btnNextMonth = new RelativeLayout(context);
        imgNextArrow = new ImageView(context);
        selectedMonth = new TextView(context);
        weekNumBar = new LinearLayout(context);
        datePickerTable = new DatePickerTable(context);

        addLeftArrow();
        addSelectedYear();
        addPriorBtn();

        addRightArrow();
        addSelectedMonth();
        addNextBtn();

        addMonthSelectBar();

        addWeekBar();

        addDatePickerTable();

    }

    private void addLeftArrow() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(48, 48);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.leftMargin = (int) mMarginPaddingLeft;
        imgPriorArrow.setLayoutParams(lp);
        imgPriorArrow.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selector_arrow_left));

        btnPriorMonth.addView(imgPriorArrow);
    }

    private void addRightArrow() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(48, 48);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.rightMargin = (int) mMarginPaddingRight;
        imgNextArrow.setLayoutParams(lp);
        imgNextArrow.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selector_arrow_right));

        btnNextMonth.addView(imgNextArrow);
    }

    private void addSelectedYear() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        selectedYear.setLayoutParams(lp);
        selectedYear.setTextColor(mSeletedYearMonthTextColor);
        selectedYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSeletedYearMonthTextSize);

        btnPriorMonth.addView(selectedYear);
    }

    private void addSelectedMonth() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        selectedMonth.setLayoutParams(lp);
        selectedMonth.setTextColor(mSeletedYearMonthTextColor);
        selectedMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSeletedYearMonthTextSize);

        btnNextMonth.addView(selectedMonth);
    }

    private void addPriorBtn() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        btnPriorMonth.setLayoutParams(lp);
        btnPriorMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                priorMonth();
            }
        });
        monthSelectBar.addView(btnPriorMonth);
    }

    private void addNextBtn() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        btnNextMonth.setLayoutParams(lp);
        btnNextMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth();
            }
        });
        monthSelectBar.addView(btnNextMonth);
    }

    private void addMonthSelectBar() {
        monthSelectBar.setOrientation(LinearLayout.HORIZONTAL);
        monthSelectBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) mMonthSelectBarHeight));

        addView(monthSelectBar);
    }

    private void addWeekBar() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mWeekNumBarHeight);
        lp.leftMargin = (int) mMarginPaddingLeft;
        lp.rightMargin = (int) mMarginPaddingRight;
        weekNumBar.setLayoutParams(lp);

        weekNumBar.setOrientation(LinearLayout.HORIZONTAL);
        weekNumBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.WeekBarBgColor));

        for (int i = 0; i < DAYS; i++) {
            TextView dayItem = new TextView(getContext());
            LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            itemLp.weight = 1;
            dayItem.setLayoutParams(itemLp);
            dayItem.setGravity(Gravity.CENTER);
            selectedYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, mWeekNumberTextSize);
            dayItem.setText(dayItemNames[i]);

            weekNumBar.addView(dayItem);
        }

        addView(weekNumBar);
    }

    private void addDatePickerTable() {
        LinearLayout.LayoutParams datePickerTableLayoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        datePickerTableLayoutParams.leftMargin = (int) mMarginPaddingLeft;
        datePickerTableLayoutParams.rightMargin = (int) mMarginPaddingRight;
        datePickerTable.setLayoutParams(datePickerTableLayoutParams);

        addView(datePickerTable);
    }

    /**
     * 设置当前年份
     *
     * @param year
     */
    public DatePickerLayout setCurrentYear(int year) {
        this.mYear = year;
        return this;
    }

    /**
     * 设置当前月份
     *
     * @param month
     */
    public DatePickerLayout setCurrentMonth(int month) {
        this.mMonth = month;
        return this;
    }

    /**
     * 初始化 DatePicker
     */
    public void init() {
        post(new Runnable() {
            @Override
            public void run() {
                selectedYear.setText(String.format("%s 年", mYear));
                selectedMonth.setText(String.format("%s 月", mMonth));
            }
        });
        loadCurTableData();
    }


    public interface DatePickerPriorNextListener {
        void setPriorListener();

        void setNextListener();
    }

    public void setDatePickerPriorNextListener(DatePickerPriorNextListener listener) {
        this.listener = listener;
    }

    public void priorMonth() {
        if (mMonth - DateUtil.getCurrentMonth() > 0) {
            setCurrentMonth(mMonth - 1);
            refreshMonth();
            loadCurTableData();
            monthChangeListener.setMonthChangeListener(mYear, mMonth);
        }
    }

    public void nextMonth() {
        setCurrentMonth(mMonth + 1);
        refreshMonth();
        loadCurTableData();
        monthChangeListener.setMonthChangeListener(mYear, mMonth);
    }

    private void refreshMonth() {
        post(new Runnable() {
            @Override
            public void run() {
                selectedMonth.setText(String.format("%s 月", mMonth));
            }
        });
    }

    /**
     * 加载当前月份的日历数据
     */
    private void loadCurTableData() {
        List<DateItem> datas = new ArrayList<>();
        int curDayOfMonth = DateUtil.getDayOfMonth(mYear, mMonth);
        try {
            firstDayOfMonth = DateUtil.getCurrentDate(mYear, mMonth);
            for (int i = 0; i < firstDayOfMonth; i++) {
                DateItem dateItem = new DateItem();
                dateItem.state = Config.STATE_EMPTY;
                datas.add(dateItem);
            }
            for (int j = 0; j < curDayOfMonth; j++) {
                DateItem day = new DateItem();
                day.day = j + 1;
                day.state = Config.STATE_EMPTY;
                datas.add(day);
            }
            datePickerTable.addDatas(datas);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // 设置多选监听器
    public void setDateItemOnClickListener(DatePickerTable.DateItemOnClickListener dateItemOnClickListener) {
        datePickerTable.setDateItemOnClickListener(dateItemOnClickListener);
    }

    public void unbindDateItemOnClickListener() {
        datePickerTable.unbindDateItemOnClickListener();
    }

    // 设置单选监听器
    public void setSingleDateItemOnClickListener(DatePickerTable.SingleDateItemOnClickListener singleDateItemOnClickListener) {
        datePickerTable.setSingleDateItemOnClickListener(singleDateItemOnClickListener);
    }

    public void unBindSingleDateItemOnClickListener() {
        datePickerTable.unbindSingleDateItemOnClickListener();
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public int getCurrentYear() {
        return mYear;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public int getCurrentMonth() {
        return mMonth;
    }

    /**
     * 获取选择的天数
     *
     * @return
     */
    public List<Integer> getSelectedDays() {
        return datePickerTable.getSelectedDays();
    }

    /**
     * 获取选择的单个日期
     *
     * @return
     */
    public int getSelectedDay() {
        return datePickerTable.getSelectedDay();
    }

    /**
     * 设置 "忙"
     *
     * @param dateStrList
     */
    public void setSchedule(List<String> dateStrList) {
        List<DateItem> datas = new ArrayList<>();
        for (String item : dateStrList) {
            try {
                DateItem dateItem = new DateItem();
                dateItem.day = DateUtil.getDayInDate(item);
                datas.add(dateItem);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        datePickerTable.refreshSchedule(firstDayOfMonth, datas);
    }

    public interface MonthChangeListener {
        void setMonthChangeListener(int year, int month);
    }

    public void setMonthChangeListener(MonthChangeListener monthChangeListener) {
        this.monthChangeListener = monthChangeListener;
    }
}
