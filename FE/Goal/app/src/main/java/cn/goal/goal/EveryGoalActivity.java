package cn.goal.goal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.goal.goal.util.DisplayUtil;

public class EveryGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_goal);
        DisplayUtil.setTranslucentStatus(this);
    }
}
