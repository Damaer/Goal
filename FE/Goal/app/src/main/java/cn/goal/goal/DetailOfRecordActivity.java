package cn.goal.goal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.goal.goal.utils.Comment_for_record;
import cn.goal.goal.utils.MylistView_for_goal_comment;

public class DetailOfRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,MyAdapter_for_every_comment.Callback {
    private MylistView_for_goal_comment listView;
    private List<Comment_for_record> listems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_record);
        listems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Comment_for_record one = new Comment_for_record(R.mipmap.ic_launcher,"用户名"+(i+1),"用户名"+(i+2),getString(R.string.test_comment),"2016年2月1日12:30");
            listems.add(one);
        }
        listView= (MylistView_for_goal_comment) findViewById(R.id.listview_for_comment);
        MyAdapter_for_every_comment myadater=new MyAdapter_for_every_comment(this,listems,this);
        listView.setAdapter(myadater);
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
                DetailOfRecordActivity.this,
                "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
                        + listems.get((Integer) v.getTag()).getuser_name(),
                Toast.LENGTH_SHORT).show();
    }
}
