package cn.goal.goal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EveryGoalActivity extends AppCompatActivity {
    private ListView listView;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_goal);
       List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
           Goal_record_class one = new Goal_record_class(R.mipmap.ic_launcher,"用户名"+1,"发送时间",i+1);
            listem.put("head",one.getUser_Image());
            listem.put("name", one.getUser_name());
            listem.put("record", one.getUser_record());
            listem.put("time",one.getTime_of_send());
            listem.put("like",one.getImageId_btn_like());
            listem.put("sum_of_like",one.getSum_of_like());
            listem.put("share",one.getImageId_btn_share());
            listem.put("reply",one.getImageId_btn_reply());
            listems.add(listem);
        }
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.listview_record_item, new String[]{"head", "name", "record","time","like","sum_of_like","share","reply"}
                ,new int[]{R.id.head_photo,R.id.user_name,R.id.content_of_record,R.id.time_of_send,R.id.like,R.id.sum_of_like,R.id.share,R.id.reply});
        listView= (ListView)findViewById(R.id.every_record_listview);
      listView.setAdapter(simplead);
        setListViewHeight(listView);//设置listview的高度，在设置adapter之后设置高度

        scrollView = (ScrollView)findViewById(R.id.scrollview);

        scrollView.post(runnable);//设置scrollView的显示位置
    }
    /**
     * 设置Listview的高度
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    /**
     * 设置scrollview显示位置，解决scrollview嵌套listview页面过长时显示问题
     */
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            EveryGoalActivity.this.scrollView.scrollTo(1,1);// 改变滚动条的位置
        }
    };
}
