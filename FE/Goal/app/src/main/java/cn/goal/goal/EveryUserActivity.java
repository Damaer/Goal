package cn.goal.goal;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goal.goal.utils.MylistView_for_goal;

import static cn.goal.goal.R.id.listview;

public class EveryUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private TextView goalname;
    private TextView time_of_send;
    private TextView sum_of_people;
    private MylistView_for_goal list;
    private ImageButton comeback;
    private ImageButton setting;
    private ScrollView scrollView;
    private PopupMenu mPopupMenu;
    private EditText content_of_send;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private TextView hide_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_user);
        goalname= (TextView) findViewById(R.id.goal_name_of_user);

        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("goalname","goal#目标名字#"+i);
            listem.put("time_of_set","2016年1月1日");
            listem.put("sum_of_people","205人");
            listems.add(listem);
        }
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.listview_goal_of_user, new String[]{"goalname","time_of_set","sum_of_people"}
                ,new int[]{R.id.goal_name_of_user,R.id.time_of_setgoal,R.id.sum_of_people});
        initlistener();
        setting= (ImageButton) findViewById(R.id.setting_for_user);

        mPopupMenu = new PopupMenu(this,setting);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.user_setting, mPopupMenu.getMenu());
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.show();
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        if(title.equals("发信息"))
                        {

                            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            rl_comment.setVisibility(View.VISIBLE);
                            scrollView.smoothScrollTo(0,0);
                        }
                        return true;
                    }
                });
            }
        });

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_send.getWindowToken(), 0);
                rl_comment.setVisibility(View.GONE);
            }
        });
        list= (MylistView_for_goal) findViewById(R.id.listview_of_user_goal);
        list.setOnItemClickListener(this);
        list.setAdapter(simplead);
    }
    public void initlistener()
    {
        goalname= (TextView) findViewById(R.id.goal_name_of_user);
        comeback=(ImageButton) findViewById(R.id.comeback_from_user);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scrollView= (ScrollView) findViewById(R.id.scrollview_for_user);
        scrollView.smoothScrollTo(0,0);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        content_of_send= (EditText) findViewById(R.id.comment_content);
        send_comment= (Button) findViewById(R.id.comment_send_message);
        hide_down= (TextView) findViewById(R.id.hide_down);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
