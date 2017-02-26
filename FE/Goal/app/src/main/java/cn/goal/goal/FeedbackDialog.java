package cn.goal.goal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import cn.goal.goal.services.Feedback;
import cn.goal.goal.utils.NetWorkUtils;

/**
 * Created by chenlin on 18/02/2017.
 */
public class FeedbackDialog implements View.OnClickListener {
    private Context context;
    private View mView;
    private Button feedback;
    private TextInputEditText suggestion;
    private TextInputEditText contact;
    private Dialog mDialog;

    public FeedbackDialog(Context context) {
        this.context = context;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null);

        suggestion = (TextInputEditText) mView.findViewById(R.id.suggestion);
        contact = (TextInputEditText) mView.findViewById(R.id.contact);

        feedback = (Button) mView.findViewById(R.id.confirm);
        feedback.setOnClickListener(this);

        mDialog = new Dialog(context);
        mDialog.setContentView(mView);
        mDialog.show();

        // 设置dialog宽度占满屏幕
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        window.setAttributes(lp);
    }

    public void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(suggestion.getText())) {
            Toast.makeText(context, "建议不能为空", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (NetWorkUtils.isNetworkConnected(context)) {
            new FeedbackTask(suggestion.getText().toString(), contact.getText().toString()).execute();
        } else {
            Toast.makeText(context, "当前网络不可用,请稍后再试.", Toast.LENGTH_SHORT).show();
        }
    }

    private class FeedbackTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog dialog;
        private String content;
        private String contact;

        public FeedbackTask(String content, String contact) {
            super();
            this.content = content;
            this.contact = contact;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new LoadingDialog().showLoading(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            return Feedback.submitFeedback(content, contact);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.closeDialog();

            if (s == null) {
                Toast.makeText(context, "感谢您的支持！", Toast.LENGTH_SHORT).show();
                closeDialog();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("提交反馈失败，是否重新尝试?");
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new FeedbackTask(content, contact).execute();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dialog != null) {
                dialog.closeDialog();
            }
        }
    }
}
