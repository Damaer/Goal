package cn.goal.goal.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import cn.goal.goal.R;
import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.utils.Util;

import java.util.Calendar;
import java.util.Date;

import static cn.goal.goal.utils.Util.dateToString;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalEditActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton buttonBack;
    ImageButton buttonConfirm;

    EditText titleView; // 标题
    EditText contentView; // 内容
    EditText beginView; // 开始时间
    EditText planView; // 计划结束时间
    RadioButton finishedView; // 已完成
    RadioButton unfinishedView; // 未完成

    Date begin;
    Date plan;

    String goalIndex;
    GoalUserMap goal;

    Calendar beginCalendar;
    Calendar planCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_edit);

        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goalIndex = getIntent().getExtras().getString("goalIndex");
        goal = GoalUserMapService.getGoal(goalIndex);

        buttonBack = (ImageButton) findViewById(R.id.button_back);
        buttonBack.setOnClickListener(this);
        buttonConfirm = (ImageButton) findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(this);

        beginView = (EditText) findViewById(R.id.begin);
        beginView.setOnClickListener(this);
        planView = (EditText) findViewById(R.id.plan);
        planView.setOnClickListener(this);

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);
        finishedView = (RadioButton) findViewById(R.id.radioButtonFinished);
        unfinishedView = (RadioButton) findViewById(R.id.radioButtonUnfinished);

        beginCalendar = Calendar.getInstance();
        planCalendar = Calendar.getInstance();

        renderInitialData();
    }

    private void renderInitialData() {
        titleView.setText(goal.getGoal().getTitle());
        contentView.setText(goal.getGoal().getContent());
        begin = goal.getBegin();
        beginView.setText(Util.dateToString(begin));
        plan = goal.getPlan();
        planView.setText(Util.dateToString(plan));

        finishedView.setChecked(goal.getFinish());
        unfinishedView.setChecked(goal.getFinish());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_confirm:
                handleEdit();
                break;
            case R.id.begin:
                handlePickBeginDate();
                break;
            case R.id.plan:
                handlePickPlanDate();
                break;
        }
    }

    private void handleEdit() {
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

        GoalUserMapService.updateGoal(goal, begin, plan, false);
        // 标记目标完成
        if (finishedView.isChecked() && !goal.getFinish()) {
            GoalUserMapService.markFinished(goal);
        } else if (unfinishedView.isChecked() && goal.getFinish()) {
            // 标记目标未完成
            GoalUserMapService.markUnfinished(goal);
        }

        finish();
    }

    private void handlePickBeginDate() {
        new DatePickerDialog(GoalEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                beginCalendar.set(Calendar.YEAR, year);
                beginCalendar.set(Calendar.MONTH, monthOfYear);
                beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                beginView.setText(dateToString(beginCalendar.getTime()));
                begin = beginCalendar.getTime();
            }
        }, beginCalendar.get(Calendar.YEAR), beginCalendar.get(Calendar.MONTH), beginCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handlePickPlanDate() {
        new DatePickerDialog(GoalEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                planCalendar.set(Calendar.YEAR, year);
                planCalendar.set(Calendar.MONTH, monthOfYear);
                planCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                planView.setText(dateToString(planCalendar.getTime()));
                plan = planCalendar.getTime();
            }
        }, planCalendar.get(Calendar.YEAR), planCalendar.get(Calendar.MONTH), planCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
