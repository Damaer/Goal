package cn.goal.goal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cn.goal.goal.services.UserService;

import java.util.Calendar;

import static cn.goal.goal.utils.Util.dateToString;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalAddActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton buttonBack;
    ImageButton buttonConfirm;

    EditText titleView; // 标题
    EditText contentView; // 内容
    EditText beginView; // 开始时间
    EditText planView; // 计划结束时间

    Calendar beginCalendar;
    Calendar planCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add);

        buttonBack = (ImageButton) findViewById(R.id.button_back);
        buttonBack.setOnClickListener(this);
        buttonConfirm = (ImageButton) findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(this);

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);
        beginView = (EditText) findViewById(R.id.begin);

        beginView.setOnClickListener(this);
        planView = (EditText) findViewById(R.id.plan);
        planView.setOnClickListener(this);

        beginCalendar = Calendar.getInstance();
        planCalendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_confirm:
                handleCreateGoal();
                break;
            case R.id.begin:
                handlePickBeginDate();
                break;
            case R.id.plan:
                handlePickPlanDate();
                break;
        }
    }

    private void handleCreateGoal() {
        Boolean error = false;
        EditText errorView = null;
        if (TextUtils.isEmpty(titleView.getText())) {
            error = true;
            errorView = titleView;
        } else if (TextUtils.isEmpty(contentView.getText())) {
            error = true;
            errorView = contentView;
        } else if (TextUtils.isEmpty(beginView.getText())) {
            error = true;
            errorView = beginView;
        } else if (TextUtils.isEmpty(planView.getText())) {
            error = true;
            errorView = planView;
        }
        if (error) {
            errorView.setError(getResources().getString(R.string.error_field_required));
            return ;
        }

        UserService.createGoal(
                titleView.getText().toString(),
                contentView.getText().toString(),
                beginView.getText().toString(),
                planView.getText().toString());
        finish();
    }

    private void handlePickBeginDate() {
        new DatePickerDialog(GoalAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                beginCalendar.set(Calendar.YEAR, year);
                beginCalendar.set(Calendar.MONTH, monthOfYear);
                beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                beginView.setText(dateToString(beginCalendar.getTime()));
            }
        }, beginCalendar.get(Calendar.YEAR), beginCalendar.get(Calendar.MONTH), beginCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handlePickPlanDate() {
        new DatePickerDialog(GoalAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                planCalendar.set(Calendar.YEAR, year);
                planCalendar.set(Calendar.MONTH, monthOfYear);
                planCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                planView.setText(dateToString(planCalendar.getTime()));
            }
        }, planCalendar.get(Calendar.YEAR), planCalendar.get(Calendar.MONTH), planCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
