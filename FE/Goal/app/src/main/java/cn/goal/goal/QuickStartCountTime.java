package cn.goal.goal;
// Created by LJF on 2017/2/23.

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

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


        }
    }

    public void onBackPressed() {
        if(cancelCount.getText().equals("确定")){
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

    private void onButtonPressed() {
        if(cancelCount.getText().equals("确定")){
            if(NetWorkUtils.isNetworkConnected(this)){
                new UploadFocusTimeTask().execute();
            } else {
                // 无网状态先保存到本地
                saveInLocal();
            }
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("确认放弃吗？")
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

    class UploadFocusTimeTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(QuickStartCountTime.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return FocusTimeService.addFocusTime(new Date(), TimeViewComm.SumOfMinutes);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err != null) {
                saveInLocal();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }

    private void saveInLocal() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        HashSet<String> focusTime = (HashSet<String>) sp.getStringSet("focus_time", new HashSet<String>());
        focusTime.add(String.valueOf(new Date().getTime()) + "-" + String .valueOf(TimeViewComm.SumOfMinutes));
        sp.edit().putStringSet("focus_time", focusTime).apply();
    }
}
