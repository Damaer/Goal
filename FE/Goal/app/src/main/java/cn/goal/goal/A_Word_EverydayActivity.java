package cn.goal.goal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class A_Word_EverydayActivity extends AppCompatActivity {
    private ImageButton comeback;
    private ImageButton share;
    private PopupWindow pop;
    private View shareView;
    private ImageButton wechat;
    private SelectPicPopupWindow menuWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__word__everyday);
        comeback= (ImageButton) findViewById(R.id.comeback);
        share=(ImageButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(A_Word_EverydayActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(A_Word_EverydayActivity.this.findViewById(R.id.activity_a__word__everyday), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                //设置layout在PopupWindow中显示的位置
                //初始化分享的ImageButton如下
                wechat= (ImageButton) findViewById(R.id.wechat);
                wechat.setOnClickListener(this);

            }
        });
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.wechat:
                    break;
                default:
                    break;
            }


        }

    };
}
