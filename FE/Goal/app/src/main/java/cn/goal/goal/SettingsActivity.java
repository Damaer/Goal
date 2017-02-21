package cn.goal.goal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.goal.goal.services.UserService;

/**
 * Created by chenlin on 20/02/2017.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int LOGOUT = 10;

    private ImageButton backButton;
    private RelativeLayout accountSafetyButton;
    private RelativeLayout aboutButton;
    private RelativeLayout feedbackButton;
    private RelativeLayout logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        backButton = (ImageButton) findViewById(R.id.back);
        backButton.setOnClickListener(this);

        accountSafetyButton = (RelativeLayout) findViewById(R.id.account_safe);
        accountSafetyButton.setOnClickListener(this);

        aboutButton = (RelativeLayout) findViewById(R.id.about);
        aboutButton.setOnClickListener(this);

        feedbackButton = (RelativeLayout) findViewById(R.id.feedback);
        feedbackButton.setOnClickListener(this);

        logoutButton = (RelativeLayout) findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.account_safe:
                startActivity(new Intent(this, AccountSafetyActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.feedback:
                new FeedbackDialog(this);
                break;
            case R.id.logout:
                handleLogout();
                break;
        }
    }

    public void handleLogout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("退出账号");
        dialog.setMessage("确认退出账号?");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new LogoutTask().execute();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class LogoutTask extends AsyncTask<Void, Void, String> {
        LoadingDialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = new LoadingDialog().showLoading(SettingsActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            UserService.logout();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            openLoginActivity();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            openLoginActivity();
        }

        private void openLoginActivity() {
            loadingDialog.closeDialog();
            // 清除当前账号信息;
            UserService.removeLocalInfo();

            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            setResult(LOGOUT);
            finish();
        }
    }
}
