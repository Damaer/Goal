package cn.goal.goal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import cn.goal.goal.util.DisplayUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView login;
    public TextView userAgreement;
    public Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DisplayUtil.setTranslucentStatus(this);
        init();
    }
    public void init(){
        login= (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);
        register= (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        userAgreement= (TextView) findViewById(R.id.userAgreement);
        userAgreement.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:

                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                break;
            case R.id.userAgreement:
                startActivity(new Intent(RegisterActivity.this,UserAgreementActivity.class));
                break;

            default:
                break;
        }
    }
}
