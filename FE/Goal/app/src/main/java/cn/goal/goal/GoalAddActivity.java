package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalAddActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 0;

    ImageButton buttonBack;
    ImageButton buttonConfirm;

    EditText title; // 标题
    EditText content; // 内容
    EditText begin; // 开始时间
    EditText plan; // 计划结束时间

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add);

        buttonBack = (ImageButton) findViewById(R.id.button_back);
        buttonConfirm = (ImageButton) findViewById(R.id.button_confirm);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        begin = (EditText) findViewById(R.id.begin);
        plan = (EditText) findViewById(R.id.plan);

        addListener();
    }

    private void addListener() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString("title", title.getText().toString());
                bundle.putString("content", content.getText().toString());
                bundle.putString("begin", begin.getText().toString());
                bundle.putString("plan", plan.getText().toString());

                intent.putExtra("data", bundle);
                setResult(REQUEST_CODE, intent);
                finish();
            }
        });
    }
}
