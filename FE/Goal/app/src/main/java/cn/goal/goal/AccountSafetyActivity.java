package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.goal.goal.util.DisplayUtil;

/**
 * Created by chenlin on 03/05/2017.
 */
public class AccountSafetyActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safety);

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email:
                break;
            case R.id.phone:
                break;
            case R.id.weibo:
                break;
            case R.id.wechat:
                break;
            case R.id.qq:
                break;
            case R.id.changePassword:
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
        SettingsListItemView email = (SettingsListItemView) findViewById(R.id.email);
        email.setOnClickListener(this);
        SettingsListItemView phone = (SettingsListItemView) findViewById(R.id.phone);
        phone.setOnClickListener(this);
        SettingsListItemView weibo = (SettingsListItemView) findViewById(R.id.weibo);
        weibo.setOnClickListener(this);
        SettingsListItemView wechat = (SettingsListItemView) findViewById(R.id.wechat);
        wechat.setOnClickListener(this);
        SettingsListItemView qq = (SettingsListItemView) findViewById(R.id.qq);
        qq.setOnClickListener(this);
        SettingsListItemView changePassword = (SettingsListItemView) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);

        DisplayUtil.setTranslucentStatus(this);
    }
}
