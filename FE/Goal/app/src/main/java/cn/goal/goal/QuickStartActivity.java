package cn.goal.goal;
// Create by LJF on 2017/02/22
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import cn.qqtheme.framework.picker.TimePicker;

public class QuickStartActivity extends QuickStartBaseActivity {

    @Override
    protected View getContentView() {
        return inflateView(R.layout.fragment_goal);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
    }

    @Override
    public void onBackPressed() {
        QuickStartManager.getInstance().exitApp();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);
        picker.setRangeEnd(23, 0);
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
//                showToast(hour + ":" + minute);
                Intent intent = new Intent(QuickStartActivity.this, QuickStartCountTime.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                startActivity(intent);
            }
        });
        picker.show();
    }

}
