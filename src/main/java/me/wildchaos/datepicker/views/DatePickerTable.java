package me.wildchaos.datepicker.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.wildchaos.datepicker.Config;
import me.wildchaos.datepicker.R;
import me.wildchaos.datepicker.model.DateItem;

/**
 * Created by 孙俊伟 on 2016/2/25.
 */
public class DatePickerTable extends RecyclerView {

    private DatePickerAdapter mAdapter;

    private List<Integer> seletedDays = new ArrayList<>();

    private int selectedDay = 0;

    private GridLayoutManager mLayoutManager;

    public DateItemOnClickListener dateItemOnClickListener;

    private SingleDateItemOnClickListener singleDateItemOnClickListener;

    public DatePickerTable(Context context) {
        super(context);
        init();
    }

    public DatePickerTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePickerTable(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private class DatePickerAdapter extends RecyclerView.Adapter {

        private LayoutInflater mInflater;
        private List<DateItem> datas = new ArrayList<>();

        public DatePickerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        private class DatePickerViewHolder extends RecyclerView.ViewHolder {

            private DayView mDayView;
            private TextView mState;
            private TextView mDay;

            public DatePickerViewHolder(final View itemView) {
                super(itemView);

                mDayView = (DayView) itemView.findViewById(R.id.day_view);
                mState = (TextView) itemView.findViewById(R.id.tv_state);
                mDay = (TextView) itemView.findViewById(R.id.tv_day);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 多选
                        if (dateItemOnClickListener != null) {
                            dateItemOnClickListener.setDateItemOnClickListener(v, dateItemOnClickListener);
                            seletedDays.add(((DayView) v).getItemDay());
                        }
                        // 单选
                        if (singleDateItemOnClickListener != null) {
                            singleDateItemOnClickListener.setDateItemOnClickListener(v, singleDateItemOnClickListener);
                            selectedDay = ((DayView) v).getItemDay();
                        }
                    }
                });


            }

            public void setLineColor() {
                ((DayView) itemView).setObliqueLineColor(R.color.colorWhite);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DatePickerViewHolder(mInflater.inflate(R.layout.item_day, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DatePickerViewHolder viewHolder = (DatePickerViewHolder) holder;
            DateItem dateItem = datas.get(position);
            viewHolder.mState.setText(dateItem.state);
            int day = dateItem.day;
            viewHolder.mDay.setText(day == 0 ? "" : String.valueOf(day));
            viewHolder.mDay.setTextColor(ContextCompat.getColor(getContext(), R.color.DayGreyColor));
            if (TextUtils.equals(dateItem.state, Config.STATE_EMPTY)) {
                viewHolder.mDayView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            }
            if (TextUtils.equals(dateItem.state, Config.STATE_BUSY)) {
                viewHolder.mDayView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBusy));
            }
            if (TextUtils.equals(dateItem.state, Config.STATE_FULL)) {
                viewHolder.mDayView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFull));
            }
            viewHolder.setLineColor();
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        private void addDatas(List<DateItem> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

    }

    private void init() {
        mAdapter = new DatePickerAdapter(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 7);
        setLayoutManager(mLayoutManager);
        setAdapter(mAdapter);
    }

    public void addDatas(List<DateItem> datas) {
        mAdapter.addDatas(datas);
    }

    public interface DateItemOnClickListener {
        void setDateItemOnClickListener(View v, DateItemOnClickListener dateItemOnClickListener);
    }

    public void setDateItemOnClickListener(DateItemOnClickListener dateItemOnClickListener) {
        this.dateItemOnClickListener = dateItemOnClickListener;
    }

    public void unbindDateItemOnClickListener() {
        this.dateItemOnClickListener = null;
    }

    public interface SingleDateItemOnClickListener {
        void setDateItemOnClickListener(View v, SingleDateItemOnClickListener SingleDateItemOnClickListener);
    }

    public void setSingleDateItemOnClickListener(SingleDateItemOnClickListener singleDateItemOnClickListener) {
        this.singleDateItemOnClickListener = singleDateItemOnClickListener;
    }

    public void unbindSingleDateItemOnClickListener() {
        this.singleDateItemOnClickListener = null;
    }


    public List<Integer> getSelectedDays() {
        return seletedDays;
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public void refreshSchedule(int firstDay, List<DateItem> datas) {
        seletedDays.clear();

        for (DateItem item1 : mAdapter.datas) {
            item1.state = Config.STATE_EMPTY;
        }
        for (DateItem item2 : datas) {
            int index = firstDay + item2.day - 1;
            DateItem dayInCalender = mAdapter.datas.get(index);
            dayInCalender.state = Config.STATE_FULL;
            mAdapter.notifyDataSetChanged();
        }

    }

}
