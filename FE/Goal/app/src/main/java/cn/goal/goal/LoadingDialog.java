package cn.goal.goal;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chenlin on 16/02/2017.
 */
public class LoadingDialog {
    private Dialog mDialog;

    public LoadingDialog showLoading(Context context) {
        View mView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_loading, null);

        mDialog = new Dialog(context, R.style.LoadingDialogStyle);
        mDialog.setContentView(mView);
        mDialog.setCanceledOnTouchOutside(false);

        mDialog.show();
        return this;
    }

    public void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
