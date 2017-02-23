package cn.goal.goal;
// Created by LJF on 2017/2/23.

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class QuickStartCountTime extends AppCompatActivity {

    private ImageButton comeback;
    private Button cancelCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_start_count_down);
        TimeViewComm time1 = (TimeViewComm) findViewById(R.id.time1);

        Intent intent = getIntent();
        int hour = Integer.parseInt(intent.getStringExtra("hour"));
        int minute = Integer.parseInt(intent.getStringExtra("minute"));

        time1.startTime(hour, minute, 0);

        comeback=(ImageButton) findViewById(R.id.comeback);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancelCount=(Button) findViewById(R.id.cancelCount);
        cancelCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
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
