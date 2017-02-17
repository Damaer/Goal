package cn.goal.goal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.resource;

/**
 * Created by Tommy on 2017/2/15.
 */

public class note_list_display  extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        listView = (ListView)findViewById(R.id.note_listView);
        dataList = new ArrayList<Map<String,Object>>();
       simple_adapter = new SimpleAdapter(this,getData(),resource,new String[]{"noteList_text","noteList_img"},new int[]{R.id.noteList_text,R.id.noteList_img});
        listView.setAdapter(simple_adapter);


    }

    private List<Map<String, Object>> getData(){
        for (int i = 0;i<20;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("noteList_text",R.string.note_list_text_test);
            map.put("noteList_img",R.drawable.ic_person_black_24dp);
        }

        return dataList;
    }
}
