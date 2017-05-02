package cn.goal.goal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView login;
    public TextView userAgreement;
    public Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        /*window.setStatusBarColor(Color.rgb(146,168,209  ));
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            //ViewCompat.setFitsSystemWindows(mChildView, false);
       }*/
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
