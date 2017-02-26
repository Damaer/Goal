package cn.goal.goal;
// Created by LJF on 2017/2/23.

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import cn.goal.goal.services.FocusTimeService;
import cn.goal.goal.utils.NetWorkUtils;
import cn.qqtheme.framework.picker.TimePicker;



public class QuickStartCountTime extends AppCompatActivity {

    private ImageButton comeback;
    public static Button cancelCount;
    private ImageButton setting;

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int hour = Integer.parseInt(intent.getStringExtra("hour"));
        int minute = Integer.parseInt(intent.getStringExtra("minute"));

        if(hour == 0 && minute == 0) {
            finish();
        }else{
            setContentView(R.layout.quick_start_count_down);
            TimeViewComm time1 = (TimeViewComm) findViewById(R.id.time1);
            time1.startTime(hour, minute, 0);

            comeback = (ImageButton) findViewById(R.id.comeback);
            comeback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            cancelCount = (Button) findViewById(R.id.cancelCount);
            cancelCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed();
                }
            });

            setting = (ImageButton) findViewById(R.id.reset_time);
            setting.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onTimePicker();
                }
            });
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("确认退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void onButtonPressed() {
        if(cancelCount.getText() == "确定"){
            if(NetWorkUtils.isNetworkConnected(this)){
                FocusTimeService.addFocusTime(new Date(), TimeViewComm.SumOfMinutes);
            }else{
                SharedPreferences sp = getSharedPreferences("sp_focus_time", Context.MODE_PRIVATE);
                sp.edit().putInt("focusTime", TimeViewComm.SumOfMinutes).commit();
            }
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("确认退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    public void onTimePicker(){
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 1);
        picker.setRangeEnd(23, 59);
        picker.setTitleText("专注到");
        picker.setSubmitText("确定");
        picker.setCancelText("取消");
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        Calendar c = Calendar.getInstance();
        final int mhour = c.get(Calendar.HOUR_OF_DAY);
        final int mminute = c.get(Calendar.MINUTE);

        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                Intent intent = new Intent(QuickStartCountTime.this, QuickStartCountTime.class);
                finish();
                int setSubNowHour = Integer.valueOf(hour) - mhour;
                int setSubNowMinute = Integer.valueOf(minute) - mminute;
                if(setSubNowHour > 0){
                    if(setSubNowMinute >= 0) {
                        intent.putExtra("minute", String.valueOf(setSubNowMinute));
                        intent.putExtra("hour", String.valueOf(setSubNowHour));
                    }else{
                        intent.putExtra("minute", String.valueOf(setSubNowMinute+60));
                        intent.putExtra("hour", String.valueOf(setSubNowHour-1));
                    }
                }else if(setSubNowHour < 0){
                    if(setSubNowMinute >= 0){
                        intent.putExtra("minute", String.valueOf(setSubNowMinute));
                        intent.putExtra("hour", String.valueOf(setSubNowHour+24));
                    }else{
                        intent.putExtra("minute", String.valueOf(setSubNowMinute+60));
                        intent.putExtra("hour", String.valueOf(setSubNowHour+23));
                    }
                }else{
                    if(setSubNowMinute >= 0){
                        intent.putExtra("minute", String.valueOf(setSubNowMinute));
                        intent.putExtra("hour", String.valueOf(setSubNowHour));
                    }else{
                        intent.putExtra("minute", String.valueOf(setSubNowMinute+60));
                        intent.putExtra("hour", String.valueOf(setSubNowHour+23));
                    }
                }
                startActivity(intent);
            }
        });
        picker.show();
    }

}
