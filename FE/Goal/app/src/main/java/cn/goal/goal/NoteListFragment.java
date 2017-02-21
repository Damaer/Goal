package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    ImageButton addNoteButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ){
            view = inflater.inflate(R.layout.note_list,container,false);
        }

        addNoteButton = (ImageButton)view.findViewById(R.id.addNoteButton) ;
        noteListView = (ListView) view.findViewById(R.id.note_listView);
        noteListAdapter = new SimpleAdapter(getContext(),getData(),R.layout.note_list_item,new String[] {"text","pic"},new int[] {R.id.noteList_text,R.id.noteList_img});

        noteListView.setAdapter(noteListAdapter);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NoteDetailAndEditActivity.class);
                startActivity(intent);
            }
        });
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
