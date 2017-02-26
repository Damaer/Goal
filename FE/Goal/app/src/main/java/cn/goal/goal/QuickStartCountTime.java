package cn.goal.goal;
// Created by LJF on 2017/2/23.

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.goal.goal.QuickStartActivity;
import cn.qqtheme.framework.picker.TimePicker;


public class QuickStartCountTime extends AppCompatActivity {

    private ImageButton comeback;
    public static Button cancelCount;
    private ImageButton setting;

    public Button getCancelCount(){ return cancelCount; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int hour = Integer.parseInt(intent.getStringExtra("hour"));
        final int minute = Integer.parseInt(intent.getStringExtra("minute"));
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
                    onBackPressed();
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

    public void onTimePicker(){
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);
        picker.setRangeEnd(23, 0);
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                Intent intent = new Intent(QuickStartCountTime.this, QuickStartCountTime.class);
                finish();
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                startActivity(intent);
            }
        });
        picker.show();
    }

    @Override
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


}
