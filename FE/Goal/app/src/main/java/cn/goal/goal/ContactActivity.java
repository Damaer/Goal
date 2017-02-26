package cn.goal.goal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.goal.goal.utils.MylistView_for_goal_record;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener, cn.goal.goal.MyAdapterForContact.Callback {
    private ListView listView;
    private List<ContactClass>listems;
    private ImageButton comeback;
    private TextView hide_down;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private EditText content_of_reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        send_comment= (Button) findViewById(R.id.comment_send_message);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        content_of_reply= (EditText) findViewById(R.id.comment_content);
        hide_down= (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_reply.getWindowToken(), 0);
            }
        });
        ;
        comeback= (ImageButton) findViewById(R.id.comeback_from_contact);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ContactClass one = new ContactClass(R.mipmap.headphoto,"用户"+i,"岁月如逝，不经意间又走到了冬季。而冬季在所有的季节里是为我所最不待见的，因为它的阴冷时常让人无法消受，更况且生命早已习惯了秋天的暖阳和惬意。","发送时间");
            listems.add(one);
        }
        listView= (ListView)findViewById(R.id.list_for_contact_mes);
        listView.setOnItemLongClickListener(this);
        MyAdapterForContact myadater=new MyAdapterForContact(this,listems,this);
        listView.setAdapter(myadater);
    }
    /**
     * 响应ListView中item的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Intent intent=new Intent(this,DetailOfRecordActivity.class);
        startActivity(intent);
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */

    public void click(View v) {
        //得到点击的是哪一个用户的名字
        String username = listems.get((Integer) v.getTag()).getUsername_for_contact();
        switch (v.getId()) {
            case R.id.user_head_photo_for_contact:
            case R.id.username_for_contact:
                Intent intent=new Intent(this,EveryUserActivity.class);
                break;
            case R.id.content_of_contact:
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String username = listems.get((Integer) view.getTag()).getUsername_for_contact();
        content_of_reply.setHint("回复"+username+"内容");
        showDialog_delete();
        return true;
    }
    private void showDialog_delete() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("删除该信息？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //点击确定之后跳转到
            }
        });
        builder.show();
    }
}
