package cn.goal.goal;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.goal.goal.utils.Comment_for_record;
import cn.goal.goal.utils.MylistView_for_goal_comment;

public class DetailOfRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,MyAdapter_for_every_comment.Callback {
    private MylistView_for_goal_comment listView;
    private List<Comment_for_record> listems;
    private ScrollView scrollView;
    private ImageButton comeback;
    private ImageView userImage;
    private TextView username;
    private TextView goalname;
    private TextView hide_down;
    private TextView content_of_record;
    private TextView content_of_send;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private ImageButton reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_record);
        listems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Comment_for_record one = new Comment_for_record(R.mipmap.ic_launcher,"用户名"+(i+1),"用户名"+(i+2),getString(R.string.test_comment),"2016年2月1日12:30");
            listems.add(one);
        }
        init_listen();
        listView= (MylistView_for_goal_comment) findViewById(R.id.listview_for_comment);
        MyAdapter_for_every_comment myadater=new MyAdapter_for_every_comment(this,listems,this);
        listView.setAdapter(myadater);
    }

    public void init_listen()
    {

        scrollView= (ScrollView) findViewById(R.id.scrollView_for_comment);
        scrollView.smoothScrollTo(0,0);
        comeback= (ImageButton) findViewById(R.id.comeback_from_detail);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userImage= (ImageView) findViewById(R.id.head_photo);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_user();
            }
        });
        username= (TextView) findViewById(R.id.user_name);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_user();
            }
        });
        goalname= (TextView) findViewById(R.id.goal_name);
        goalname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_goal();
            }
        });
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        reply= (ImageButton) findViewById(R.id.reply);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
            }
        });

        content_of_send= (TextView) findViewById(R.id.comment_content);
        send_comment= (Button) findViewById(R.id.comment_send_message);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        hide_down= (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_send.getWindowToken(), 0);
            }
        });

    }
    public void jump_to_user()
    {
        Intent intent=new Intent(this,EveryUserActivity.class);
        startActivity(intent);
    }
    public void jump_to_goal()
    {
        finish();
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int position) {
        switch (position)
        {
            case SCROLL_STATE_FLING:
                //下拉添加数据源
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int position, int i1, int i2) {

    }
    /**
     * 响应ListView中item的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 接口方法，响应ListView按钮点击事件
     */

    public void click(View v) {
        String username=listems.get((Integer) v.getTag()).getuser_name();
        Toast.makeText(
                DetailOfRecordActivity.this,
                "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
                        + listems.get((Integer) v.getTag()).getuser_name(),
                Toast.LENGTH_SHORT).show();
        switch (v.getId())
        {
            case R.id.commentuser_photo:
                jump_to_user();
                break;
            case R.id.user_reply:
                jump_to_user();
                break;
            case R.id.user_replyed:
                jump_to_user();
                break;
            case R.id.content_of_comment:
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
