package cn.goal.goal;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class UserAgreementActivity extends AppCompatActivity implements View.OnClickListener {
    public ImageButton comeback_from_useragreement;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }
    public void init(){
        comeback_from_useragreement= (ImageButton) findViewById(R.id.comeback_from_useragreement);
        comeback_from_useragreement.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.comeback_from_useragreement:
                finish();
                break;
            default:
                break;
        }

    }
}
