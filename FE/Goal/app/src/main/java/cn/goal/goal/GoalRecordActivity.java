package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Goal;

/**
 * Created by Jeffrey Wang on 17-2-21 0021.
 */
public class GoalRecordActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton buttonBack;
    TextView buttonSend;
    EditText contentView;
    int goalIndex;
    Goal goal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_record);
        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goalIndex = getIntent().getExtras().getInt("goalIndex");
        goal = UserService.getGoals().get(goalIndex);
        buttonBack= (ImageButton) findViewById(R.id.button_back);
        buttonSend= (TextView) findViewById(R.id.button_record);
        contentView = (EditText) findViewById(R.id.goal_record_content);
        buttonBack.setOnClickListener(this);
        buttonSend.setOnClickListener(this);
    }
        @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_back:
                    finish();
                    break;
                case R.id.button_record:
                    handleRecord();
                    break;
            }
    }

    private void handleRecord() {
        Boolean error = false;
        EditText errorView = null;
        if (TextUtils.isEmpty(contentView.getText())) {
            error = true;
            errorView = contentView;
        }
        if (error){
            errorView.setError(getResources().getString(R.string.error_field_required));
            return ;
        }

        // TODO: 17-2-21 0021 调用 UserService 的方法，发送记录

        finish();
    }
}
