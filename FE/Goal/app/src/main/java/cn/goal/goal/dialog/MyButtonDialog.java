package cn.goal.goal.dialog;
// Created by LJF on 2017/2/27.

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import cn.goal.goal.R;

public class MyButtonDialog extends Dialog {

    public MyButtonDialog(Context context) {
        super(context);
    }

    public MyButtonDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String buttonText;
        private View contentView;
        private DialogInterface.OnClickListener buttonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.buttonText = positiveButtonText;
            this.buttonClickListener = listener;
            return this;
        }

        public MyButtonDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MyButtonDialog dialog = new MyButtonDialog(context, R.style.MyButtonDialog);
            View layout = inflater.inflate(R.layout.dialog_button, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            if (buttonText != null) {
                if (buttonClickListener != null) {
                    ((Button) layout.findViewById(R.id.button_dialog))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    buttonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            }else {
                layout.findViewById(R.id.button_dialog).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}



