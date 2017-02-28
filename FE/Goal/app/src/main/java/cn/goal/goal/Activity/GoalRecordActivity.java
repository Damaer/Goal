package cn.goal.goal.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.goal.goal.Dialog.LoadingDialog;
import cn.goal.goal.R;
import cn.goal.goal.Services.CommentService;
import cn.goal.goal.Services.GoalUserMapService;
import cn.goal.goal.Services.object.GoalUserMap;
import cn.goal.goal.Utils.NetWorkUtils;

/**
 * Created by Jeffrey Wang on 17-2-21 0021.
 */
public class GoalRecordActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_RECORD = 1002;

    ImageButton buttonBack;
    TextView buttonSend;
    EditText contentView;
    String goalIndex;
    GoalUserMap goal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_record);
        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goalIndex = getIntent().getExtras().getString("goalIndex");
        goal = GoalUserMapService.getGoal(goalIndex);
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
                    setResult(RESULT_CANCELED);
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
        if (NetWorkUtils.isNetworkConnected(this)) {
            new RecordTask().execute();
        } else {
            Toast.makeText(GoalRecordActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    class RecordTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(GoalRecordActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return CommentService.comment(goal.getGoal(), contentView.getText().toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 添加失败
                Toast.makeText(GoalRecordActivity.this, "记录失败，请重新尝试", Toast.LENGTH_SHORT).show();
            } else {
                setResult(RESULT_OK);
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
