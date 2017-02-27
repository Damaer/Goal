package cn.goal.goal;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chenlin on 16/02/2017.
 */
public class LoadingDialog {
    private Dialog mDialog;
    private Context mContext;
    private Boolean close; // 标记是否调用closeDialog方法

    public LoadingDialog showLoading(Context context) {
        close = false;
        mContext = context;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!close) {
                    View mView = LayoutInflater.from(mContext)
                            .inflate(R.layout.dialog_loading, null);

                    mDialog = new Dialog(mContext, R.style.LoadingDialogStyle);
                    mDialog.setContentView(mView);
                    mDialog.setCanceledOnTouchOutside(false);

                    mDialog.show();
                }
            }
        }, 500);

        return this;
    }

    public void closeDialog() {
        close = true;
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
