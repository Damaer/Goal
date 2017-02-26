package cn.goal.goal;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.utils.MylistView_for_goal_record;

public class EveryGoalActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,MyAdapter_for_every_record.Callback {
    private ImageButton comeback;
    private ImageButton write;
    private MylistView_for_goal_record listView;
    private ImageButton go_goal;
    private ScrollView scrollView;
    private  List<Goal_record_class> listems;
    private PopupMenu mPopupMenu;
    private String goalIndex;
    private GoalUserMap goal; // 存放goal信息
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_goal);
        // 传入数据不正确
        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goalIndex = getIntent().getExtras().getString("goalIndex");
        goal = GoalUserMapService.getGoal(goalIndex);
       listems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
           Goal_record_class one = new Goal_record_class(R.mipmap.ic_launcher,"用户名"+(i+1),"发送时间",i+1);
            listems.add(one);


        }
        listView= (MylistView_for_goal_record)findViewById(R.id.every_record_listview);
        comeback=(ImageButton)findViewById(R.id.comeback_from_goal);
        write= (ImageButton) findViewById(R.id.write);
        go_goal= (ImageButton) findViewById(R.id.go_to_goal);
        mPopupMenu = new PopupMenu(this,write);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.write_and_setting, mPopupMenu.getMenu());
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.show();
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        if(title.equals(""))
                        {

                        }
                        return true;
                    }
                });
            }
        });
        init_listener();
        MyAdapter_for_every_record myadater=new MyAdapter_for_every_record(this,listems,this);
      listView.setAdapter(myadater);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        scrollView = (ScrollView)findViewById(R.id.scrollview);
        /*如果用户没有加入这个目标，这把goal图片设置为add.png
        *如果用户今天朝着目标去做了就把他设置为go2.png
        if()
        {

        }*/
    }
    /**
     * 初始化监听事件
     */
   public void init_listener()
    {
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        go_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1();
                //把图片设置为已点击
                //
            }
        });
    }
    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog1() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("朝着目标前进一小步");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    go_goal.setBackgroundResource(R.mipmap.go2);
                //点击确定之后跳转到
            }
        });
        builder.show();
    }
    private void showDialog2() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("是否加入这个目标？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                go_goal.setBackgroundResource(R.mipmap.go1);
                //点击确定之后跳转到
            }
        });
        builder.show();
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
               Toast.makeText(
                            EveryGoalActivity.this,
                           "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
                                       + listems.get((Integer) v.getTag()).getGoal_name(),
                               Toast.LENGTH_SHORT).show();
          }
}
