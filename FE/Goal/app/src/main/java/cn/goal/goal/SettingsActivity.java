package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.goal.goal.util.DisplayUtil;

/**
 * Created by chenlin on 03/05/2017.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_safety:
                startActivity(new Intent(this, AccountSafetyActivity.class));
                break;
            case R.id.other_about:
                break;
            case R.id.other_feedback:
                break;
            case R.id.logout:
                logout();
                break;
            default: // navigation back icon 返回
                finish();
                break;
        }
    }

    private void init() {
        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.settings_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(this);
        // add listener
        SettingsListItemView accountSafety = (SettingsListItemView) findViewById(R.id.account_safety);
        accountSafety.setOnClickListener(this);
        SettingsListItemView about = (SettingsListItemView) findViewById(R.id.other_about);
        about.setOnClickListener(this);
        SettingsListItemView feedback = (SettingsListItemView) findViewById(R.id.other_feedback);
        feedback.setOnClickListener(this);
        SettingsListItemView logout = (SettingsListItemView) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        DisplayUtil.setTranslucentStatus(this);
    }

    private void logout() {

    }
}
