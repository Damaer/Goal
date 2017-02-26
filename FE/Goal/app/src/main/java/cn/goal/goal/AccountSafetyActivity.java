package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by chenlin on 18/02/2017.
 */
public class AccountSafetyActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton closeButton;
    private RelativeLayout bindEmail;
    private RelativeLayout bindPhone;
    private RelativeLayout bindSina;
    private RelativeLayout bindQQ;
    private RelativeLayout bindWechat;
    private TextView emailBind;
    private TextView phoneBind;
    private TextView sinaBind;
    private TextView qqBind;
    private TextView wechatBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safety);

        emailBind = (TextView) findViewById(R.id.email_bind);
        phoneBind = (TextView) findViewById(R.id.phone_bind);
        sinaBind = (TextView) findViewById(R.id.sina_bind);
        qqBind = (TextView) findViewById(R.id.qq_bind);
        wechatBind = (TextView) findViewById(R.id.wechat_bind);

        closeButton = (ImageButton) findViewById(R.id.close);
        closeButton.setOnClickListener(this);

        bindEmail = (RelativeLayout) findViewById(R.id.bind_email);
        bindEmail.setOnClickListener(this);

        bindPhone = (RelativeLayout) findViewById(R.id.bind_phone);
        bindPhone.setOnClickListener(this);

        bindSina = (RelativeLayout) findViewById(R.id.bind_weibo);
        bindSina.setOnClickListener(this);

        bindQQ = (RelativeLayout) findViewById(R.id.bind_qq);
        bindQQ.setOnClickListener(this);

        bindWechat = (RelativeLayout) findViewById(R.id.bind_wechat);
        bindWechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.bind_email:
                handleBindEmail();
                break;
            case R.id.bind_phone:
                handleBindPhone();
                break;
            case R.id.bind_weibo:
                handleBindSina();
                break;
            case R.id.bind_qq:
                handleBindQQ();
                break;
            case R.id.bind_wechat:
                handleBindWechat();
                break;
        }
    }

    public void handleBindEmail() {
        Toast.makeText(this, "此功能尚未开发", Toast.LENGTH_SHORT);
    }

    public void handleBindPhone() {
        Toast.makeText(this, "此功能尚未开发", Toast.LENGTH_SHORT);
    }

    public void handleBindSina() {
        Toast.makeText(this, "此功能尚未开发", Toast.LENGTH_SHORT);
    }

    public void handleBindQQ() {
        Toast.makeText(this, "此功能尚未开发", Toast.LENGTH_SHORT);
    }

    public void handleBindWechat() {
        Toast.makeText(this, "此功能尚未开发", Toast.LENGTH_SHORT);
    }

}
