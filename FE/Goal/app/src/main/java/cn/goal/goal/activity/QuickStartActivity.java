package cn.goal.goal.activity;
// Create by LJF on 2017/02/22
import android.content.Intent;
import android.view.View;

import java.util.Calendar;

import cn.goal.goal.manager.QuickStartCountTime;
import cn.goal.goal.manager.QuickStartManager;
import cn.goal.goal.R;
import cn.qqtheme.framework.picker.TimePicker;

public class QuickStartActivity extends QuickStartBaseActivity {

    @Override
    protected View getContentView() {
        return inflateView(R.layout.fragment_goal);
    }

    @Override
    protected void setContentViewAfter(View contentView) { }

    @Override
    public void onBackPressed() {
        QuickStartManager.getInstance().exitApp();
    }

    public void onTimePicker(View view) {
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
                Intent intent = new Intent(QuickStartActivity.this, QuickStartCountTime.class);
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
