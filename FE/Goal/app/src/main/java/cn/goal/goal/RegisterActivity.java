package cn.goal.goal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton comeback;
    private Button register;
    private TextView agreement;
    private TextView goto_signin;
    private ImageButton wechat;
    private ImageButton qq;
    private ImageButton weibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private  void init()
    {
        comeback=(ImageButton) findViewById(R.id.comeback);
        comeback.setOnClickListener(this);
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(this);
        agreement= (TextView) findViewById(R.id.agreement);
        agreement.setOnClickListener(this);
        goto_signin=(TextView) findViewById(R.id.go_to_signin);
        goto_signin.setOnClickListener(this);
        wechat=(ImageButton) findViewById(R.id.wechat);
        wechat.setOnClickListener(this);
        qq=(ImageButton) findViewById(R.id.qq);
        qq.setOnClickListener(this);
        weibo=(ImageButton) findViewById(R.id.weibo);
        weibo.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.go_to_signin:
            case R.id.comeback:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                break;
            case R.id.agreement:
                Intent intent3 = new Intent();
                intent3.setClass(RegisterActivity.this, AgreementActivity.class);
                startActivity(intent3);
                break;
            case  R.id.wechat:
                break;
            case  R.id.qq:
                break;
            case R.id.weibo:
                break;

        }
    }
}
