package cn.goal.goal;
// Create by LJF on 2017/02/22
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

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
        picker.setTitleText("完成时间");
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
                intent.putExtra("hour", String.valueOf(Integer.valueOf(hour)-mhour));
                intent.putExtra("minute", String.valueOf(Integer.valueOf(minute)-mminute));
                startActivity(intent);
            }
        });
        picker.show();
    }

}
