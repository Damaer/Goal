package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommy on 2017/2/20.
 */

public class NoteListFragment extends Fragment{
    private View view;
    private ListView noteListView;
    private SimpleAdapter noteListAdapter;
    private List<Map<String,Object>> dataList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ){
            view = inflater.inflate(R.layout.note_list,container,false);
        }

        noteListView = (ListView) view.findViewById(R.id.note_listView);
        noteListAdapter = new SimpleAdapter(getContext(),getData(),R.layout.note_list_item,new String[] {"text","pic"},new int[] {R.id.noteList_text,R.id.noteList_img});

        noteListView.setAdapter(noteListAdapter);
        return view;
    }

    private List<Map<String,Object>> getData(){

        dataList = new ArrayList<>();
        for (int i = 0;i<20;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.drawable.ic_lightbulb_outline_black_24dp);
            map.put("text", "blablablablablabalblablablablablablahblahbhlabhahblah          " + i);

            dataList.add(map);
        }
        return dataList;
    }


}
