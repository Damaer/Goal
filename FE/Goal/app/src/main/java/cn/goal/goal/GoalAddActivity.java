package cn.goal.goal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cn.goal.goal.services.GoalService;
import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.Goal;
import cn.goal.goal.utils.NetWorkUtils;

import java.util.Calendar;
import java.util.Date;

import static cn.goal.goal.utils.Util.dateToString;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalAddActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton buttonBack;
    ImageButton buttonConfirm;

    AutoCompleteTextView  titleView; // 标题
    EditText contentView; // 内容
    EditText beginView; // 开始时间
    EditText planView; // 计划结束时间
    private ArrayAdapter<String> arrayAdapter;//
    Date begin;
    Date plan;

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

        titleView = (AutoCompleteTextView ) findViewById(R.id.title);
        //
        String [] arr={"aa","aab","aad","aaf","aag","aat","aat","aac","asd","dfadf","fd","dsf","rty","ret","tr"};
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        titleView.setAdapter(arrayAdapter);
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

        if (NetWorkUtils.isNetworkConnected(this)) {
            // 更新goals数据
            new CreateGoalTask(
                    titleView.getText().toString(),
                    contentView.getText().toString(),
                    begin,
                    plan
            ).execute();
        } else {
            Toast.makeText(this, "添加失败，当前网络不可用.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePickBeginDate() {
        new DatePickerDialog(GoalAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        new DatePickerDialog(GoalAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    class CreateGoalTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        private String title;
        private String content;
        private Date begin;
        private Date plan;

        public CreateGoalTask(String title, String content, Date begin, Date plan) {
            super();
            this.title = title;
            this.content = content;
            this.begin = begin;
            this.plan = plan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(GoalAddActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            Goal goal = GoalService.addGoal(title, content);

            if (goal == null) return null;

            return GoalUserMapService.addGoal(
                    goal,
                    begin,
                    plan,
                    true
            );
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 获取失败则提示用户是否重新获取
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalAddActivity.this);
                builder.setMessage("添加目标失败，是否重新尝试?");
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new CreateGoalTask(title, content, begin, plan).execute();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();
            } else { // 获取成功
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }
}
