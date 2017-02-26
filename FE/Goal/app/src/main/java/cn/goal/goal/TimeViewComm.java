package cn.goal.goal;
// Created by LJF on 2017/2/22.

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class TimeViewComm extends LinearLayout {
    protected TextView mHours;
    protected TextView mMinutes;
    protected TextView mSeconds;
    protected TextView spaceOne;
    protected TextView spaceTwo;
    protected Button mCancelCount;
    public static int SumOfTime;
    private int mTextColor = Color.WHITE;
    private int mBackgroundColor = Color.BLACK;
    private int mSpaceColor = Color.BLACK;
    private int mTextSize = 30;
    private int mRadius = 5;
    private int mPaddingHorizontal = 4;
    private int mPaddingVertical = 0;
    private GradientDrawable drawable;
    private DecimalFormat df = new DecimalFormat("00");
    private TimeoutManager mTimeoutManager;
    private TimeoutListener mListener;
    private List<TimePoint> mTimeoutPoints;

    public Button getmCancelCount(){ return mCancelCount; }

    public TimeViewComm(Context context) {
        this(context, null);
    }

    public TimeViewComm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeViewComm(Context context, AttributeSet attrs, int defStyleAttr) { this(context, attrs, defStyleAttr, 0); }

    public TimeViewComm(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, dm);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, dm);
        mPaddingHorizontal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingHorizontal, dm);
        mPaddingVertical = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingVertical, dm);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeViewComm);
        mTextColor = array.getColor(R.styleable.TimeViewComm_tvc_textColor, mTextColor);
        mBackgroundColor = array.getColor(R.styleable.TimeViewComm_tvc_backgroundColor, mBackgroundColor);
        mSpaceColor = array.getColor(R.styleable.TimeViewComm_tvc_spaceColor, mSpaceColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.TimeViewComm_tvc_textSize, mTextSize);
        mRadius = array.getDimensionPixelSize(R.styleable.TimeViewComm_tvc_radius, mRadius);
        mPaddingHorizontal = array.getDimensionPixelSize(R.styleable.TimeViewComm_tvc_textPaddingHorizantal, mPaddingHorizontal);
        mPaddingVertical = array.getDimensionPixelSize(R.styleable.TimeViewComm_tvc_textPaddingVertical, mPaddingVertical);
        array.recycle();

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        drawable = new GradientDrawable();
        drawable.setColor(mBackgroundColor);
        drawable.setCornerRadius(mRadius);

        mHours = new TextView(context);
        mHours.setLayoutParams(layoutParams);
        mHours.setTextColor(mTextColor);
        mHours.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mHours.setText("00");
        mHours.setPadding(mPaddingHorizontal, mPaddingVertical, mPaddingHorizontal, mPaddingVertical);
        mHours.setBackground(drawable);

        mMinutes = new TextView(context);
        mMinutes.setLayoutParams(layoutParams);
        mMinutes.setTextColor(mTextColor);
        mMinutes.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mMinutes.setText("00");
        mMinutes.setBackground(drawable);
        mMinutes.setPadding(mPaddingHorizontal, mPaddingVertical, mPaddingHorizontal, mPaddingVertical);

        mSeconds = new TextView(context);
        mSeconds.setLayoutParams(layoutParams);
        mSeconds.setTextColor(mTextColor);
        mSeconds.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mSeconds.setText("00");
        mSeconds.setBackground(drawable);
        mSeconds.setPadding(mPaddingHorizontal, mPaddingVertical, mPaddingHorizontal, mPaddingVertical);

//        mCancelCount = (Button)findViewById(R.id.cancelCount);

        spaceOne = new TextView(context);
        spaceOne.setLayoutParams(layoutParams);
        spaceOne.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        spaceOne.setTextColor(mSpaceColor);
        spaceOne.setText(":");

        spaceTwo = new TextView(context);
        spaceTwo.setLayoutParams(layoutParams);
        spaceTwo.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        spaceTwo.setTextColor(mSpaceColor);
        spaceTwo.setText(":");

        addView(mHours);
        addView(spaceOne);
        addView(mMinutes);
        addView(spaceTwo);
        addView(mSeconds);
    }

    public void startTime(int hour, int minutes, int second) {
        if (null == mTimeoutManager) {
            mTimeoutManager = new TimeoutManager(hour, minutes, second, new TimeoutManager.OnTimeRunListener() {
                @Override
                public void onTimeRun(int hour, int minute, int second) {
                    setTime(df.format(hour), df.format(minute), df.format(second));
                }
                public void onTimeRun(String msg){
                    updateCountTimeUI();
                }
            });
        } else {
            mTimeoutManager.resetTime(hour, minutes, second);
        }
    }

    public void updateCountTimeUI(){
        mHours.setText(null);
        mMinutes.setText(null);
        mSeconds.setText(null);
        spaceOne.setText("完成啦");
        spaceTwo.setText(null);
        mHours.setBackground(null);
        mMinutes.setBackground(null);
        mSeconds.setBackground(null);
        calculateSumOfTime();
        QuickStartCountTime.cancelCount.setText("确定");

    }

    public static int calculateSumOfTime(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int way = c.get(Calendar.DAY_OF_WEEK);
        if(way == 1 && hour == 0 && minute == 0 && second ==0){
            SumOfTime = 0;
        }else {
            ++SumOfTime;
        }
        return SumOfTime;
    }

    protected void setTime(String hour, String minute, String second) {

        mHours.setText(hour);
        mMinutes.setText(minute);
        mSeconds.setText(second);
        if (null != mListener) {
            checkTimeout();
            checkTimeoutPoint();
        }
    }

    private void checkTimeoutPoint() {
        String hour = mHours.getText().toString();
        String minute = mMinutes.getText().toString();
        String second = mSeconds.getText().toString();

        for (TimePoint timePoint : mTimeoutPoints) {
            if(timePoint.isEquals(hour, minute, second)) {
                mListener.onTimePoint(hour, minute, second);
                mTimeoutPoints.remove(timePoint);
                break;
            }
        }
    }

    private void checkTimeout() {
        if(mHours.getText().equals("00") && mMinutes.getText().equals("00") && mSeconds.getText().equals("00")) {
            Toast.makeText(getContext(), "timeout", Toast.LENGTH_SHORT).show();
        }
    }

    private class TimePoint{
        private String hour;
        private String minute;
        private String second;

        public TimePoint(String hour, String minute, String second) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        public boolean isEquals(String hour, String minute, String second) {
            return this.hour.equals(hour) && this.minute.equals(minute) && this.second.equals(second);
        }
    }

    public interface TimeoutListener {
        void onTimePoint(String hour, String minute, String second);
        void onTimeout();
    }

    public void setOnTimeoutListener(TimeoutListener listener){
        mListener = listener;
    }

    public void addTimeoutPoint(int hour, int minute, int second) {
        if (null == mTimeoutPoints) {
            mTimeoutPoints = new ArrayList<>();
        }
        TimePoint timePoint = new TimePoint(df.format(hour), df.format(minute), df.format(second));
        mTimeoutPoints.add(timePoint);
    }
}

