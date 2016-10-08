package com.slash.youth.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.slash.youth.utils.CommonUtils;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/9/22.
 */
public class SlashDateTimePicker extends RelativeLayout {
    public SlashDateTimePicker(Context context) {
        super(context);
        initView();
    }

    public SlashDateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlashDateTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    String[] monthAndDay = {"", "", "", "", ""};
    int[] monthIntArr = new int[5];
    int[] dayIntArr = new int[5];
    String[] hours = {"", "", "", "", ""};
    int[] hourIntArr = new int[5];
    String[] minutes = {"", "", "", "", ""};
    int[] minuteIntArr = new int[5];
    NumberPicker npMonthDay;
    NumberPicker npHour;
    NumberPicker npMinute;
    private Calendar mCalendar;

    int currentChooseMonth;
    int currentChooseDay;
    int currentChooseHour;
    int currentChooseMinute;

    private void initView() {
        npMonthDay = new SlashNumberPicker(CommonUtils.getContext());
        npHour = new SlashNumberPicker(CommonUtils.getContext());
        npMinute = new SlashNumberPicker(CommonUtils.getContext());

        LayoutParams paramsNpMonthDay = new LayoutParams(CommonUtils.dip2px(80), -2);
        npMonthDay.setLayoutParams(paramsNpMonthDay);

        LayoutParams paramsNpHour = new LayoutParams(-2, -2);
        paramsNpHour.addRule(RelativeLayout.CENTER_HORIZONTAL);
        npHour.setLayoutParams(paramsNpHour);

        LayoutParams paramsNpMinute = new LayoutParams(-2, -2);
        paramsNpMinute.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        npMinute.setLayoutParams(paramsNpMinute);

        addView(npMonthDay);
        addView(npHour);
        addView(npMinute);

        initData();
        initListener();
    }

    private void initData() {
        mCalendar = Calendar.getInstance();
        Date date = new Date();
        mCalendar.setTime(date);
        currentChooseMonth = mCalendar.get(Calendar.MONTH);
        currentChooseDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentChooseHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        currentChooseMinute = mCalendar.get(Calendar.MINUTE);
        initTime();

        npMonthDay.setDisplayedValues(monthAndDay);
        npHour.setDisplayedValues(hours);
        npMinute.setDisplayedValues(minutes);

        npMonthDay.setMinValue(0);
        npMonthDay.setMaxValue(4);
        npMonthDay.setValue(2);

        npHour.setMinValue(0);
        npHour.setMaxValue(4);
        npHour.setValue(2);

        npMinute.setMinValue(0);
        npMinute.setMaxValue(4);
        npMinute.setValue(2);
    }

    private void initTime() {
        mCalendar.add(Calendar.DAY_OF_MONTH, -3);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY) - 3;
        if (hour < 0) {
            hour = hour + 24;
        }
        int minute = mCalendar.get(Calendar.MINUTE) - 3;
        if (minute < 0) {
            minute = minute + 60;
        }
        for (int i = 0; i < 5; i++) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            monthIntArr[i] = month;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            dayIntArr[i] = day;
            monthAndDay[i] = month + "月" + day + "日";
            hour++;
            if (hour > 23) {
                hour = 0;
            }
            hourIntArr[i] = hour;
            if (hour < 10) {
                hours[i] = "0" + hour + "时";
            } else {
                hours[i] = hour + "时";
            }
            minute++;
            if (minute > 59) {
                minute = 0;
            }
            minuteIntArr[i] = minute;
            if (minute < 10) {
                minutes[i] = "0" + minute + "分";
            } else {
                minutes[i] = minute + "分";
            }
        }


        //恢复到当前的时间
        mCalendar.setTime(new Date());
    }

    private void initListener() {
        npMonthDay.setOnValueChangedListener(new ChooseTimeListener("NpMonthDay"));
        npHour.setOnValueChangedListener(new ChooseTimeListener("NpHour"));
        npMinute.setOnValueChangedListener(new ChooseTimeListener("NpMinute"));
    }

    public class ChooseTimeListener implements NumberPicker.OnValueChangeListener {

        String mCurrentId;

        public ChooseTimeListener(String currentId) {
            this.mCurrentId = currentId;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            int length = monthAndDay.length;
            int upVal = newVal - 2;
            int downVal = newVal + 2;
            if (upVal < 0) {
                upVal = upVal + length;
            }
            if (downVal > length - 1) {
                downVal = downVal - length;
            }

            switch (mCurrentId) {
                case "NpMonthDay":
                    boolean isUp;
                    if (oldVal == 4 && newVal == 0) {
                        isUp = true;
                    } else if (oldVal == 0 && newVal == 4) {
                        isUp = false;
                    } else if (newVal > oldVal) {
                        isUp = true;
                    } else {
                        isUp = false;
                    }
                    if (isUp) {
                        //向上滚，日期增加
                        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    } else {
                        //向下滚，日期减少
                        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    }
                    mCalendar.add(Calendar.DAY_OF_MONTH, -2);
                    int upMonth = mCalendar.get(Calendar.MONTH) + 1;
                    int upDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                    monthIntArr[upVal] = upMonth;
                    dayIntArr[upVal] = upDay;
                    monthAndDay[upVal] = upMonth + "月" + upDay + "日";

                    mCalendar.add(Calendar.DAY_OF_MONTH, 4);
                    int downMonth = mCalendar.get(Calendar.MONTH) + 1;
                    int downDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                    monthIntArr[downVal] = downMonth;
                    dayIntArr[downVal] = downDay;
                    monthAndDay[downVal] = downMonth + "月" + downDay + "日";

                    //回到当前时间
                    mCalendar.add(Calendar.DAY_OF_MONTH, -2);
                    currentChooseMonth = mCalendar.get(Calendar.MONTH);
                    currentChooseDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                    break;
                case "NpHour":
                    int chooseHour = hourIntArr[newVal];
                    hourIntArr[upVal] = chooseHour - 2;
                    if (hourIntArr[upVal] < 0) {
                        hourIntArr[upVal] = hourIntArr[upVal] + 24;
                    }
                    if (hourIntArr[upVal] < 10) {
                        hours[upVal] = "0" + hourIntArr[upVal] + "时";
                    } else {
                        hours[upVal] = hourIntArr[upVal] + "时";
                    }
                    hourIntArr[downVal] = chooseHour + 2;
                    if (hourIntArr[downVal] > 23) {
                        hourIntArr[downVal] = hourIntArr[downVal] - 24;
                    }
                    if (hourIntArr[downVal] < 10) {
                        hours[downVal] = "0" + hourIntArr[downVal] + "时";
                    } else {
                        hours[downVal] = hourIntArr[downVal] + "时";
                    }
                    currentChooseHour = chooseHour;
                    break;
                case "NpMinute":
                    int chooseMinute = minuteIntArr[newVal];
                    minuteIntArr[upVal] = chooseMinute - 2;
                    if (minuteIntArr[upVal] < 0) {
                        minuteIntArr[upVal] = minuteIntArr[upVal] + 60;
                    }
                    if (minuteIntArr[upVal] < 10) {
                        minutes[upVal] = "0" + minuteIntArr[upVal] + "分";
                    } else {
                        minutes[upVal] = minuteIntArr[upVal] + "分";
                    }
                    minuteIntArr[downVal] = chooseMinute + 2;
                    if (minuteIntArr[downVal] > 59) {
                        minuteIntArr[downVal] = minuteIntArr[downVal] - 60;
                    }
                    if (minuteIntArr[downVal] < 10) {
                        minutes[downVal] = "0" + minuteIntArr[downVal] + "分";
                    } else {
                        minutes[downVal] = minuteIntArr[downVal] + "分";
                    }
                    currentChooseMinute = chooseMinute;
                    break;
            }
        }
    }

    public class SlashNumberPicker extends NumberPicker {
        public SlashNumberPicker(Context context) {
            super(context);
        }

        public SlashNumberPicker(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SlashNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void addView(View child) {
            super.addView(child);
            updateView(child);
        }

        @Override
        public void addView(View child, int index,
                            android.view.ViewGroup.LayoutParams params) {
            super.addView(child, index, params);
            updateView(child);
        }

        @Override
        public void addView(View child, android.view.ViewGroup.LayoutParams params) {
            super.addView(child, params);
            updateView(child);
        }

        public void updateView(View view) {
            if (view instanceof EditText) {
                EditText etData = (EditText) view;
                etData.setClickable(false);
                etData.setFocusable(false);
                etData.setTextColor(0xff000000);
            }
        }

        @Override
        public void setDisplayedValues(String[] displayedValues) {
            super.setDisplayedValues(displayedValues);
            hideNumberPickerDivider();
        }

        public void hideNumberPickerDivider() {
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                try {
                    if (pf.getName().equals("mSelectionDivider")) {
                        pf.setAccessible(true);
                        pf.set(this, new ColorDrawable(0x00000000));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCurrentChooseMonth() {
        return currentChooseMonth + 1;
    }

    public int getCurrentChooseDay() {
        return currentChooseDay;
    }

    public int getCurrentChooseHour() {
        return currentChooseHour;
    }

    public int getCurrentChooseMinute() {
        return currentChooseMinute;
    }

    public int getCurrentChooseYear() {
        return mCalendar.get(Calendar.YEAR);
    }
}
