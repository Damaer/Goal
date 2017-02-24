package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Note;

/**
 * Created by Tommy on 2017/2/20.
 */

public class NoteListFragment extends Fragment {
    private View view;
    private ListView noteListView;
    private SimpleAdapter noteListAdapter;
    private List<Map<String,String>> dataList;
    ImageButton addNoteButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ){
            view = inflater.inflate(R.layout.note_list,container,false);
        }

        addNoteButton = (ImageButton)view.findViewById(R.id.addNoteButton) ;
        noteListView = (ListView) view.findViewById(R.id.note_listView);

        createListView();
        addListener();

        return view;
    }

    private void createListView(){
        noteListAdapter = new SimpleAdapter(getContext(),getData(), R.layout.note_list_item,new String[] {"text","time"},new int[] {R.id.noteList_text, R.id.noteList_time});

        noteListView.setAdapter(noteListAdapter);
    }

    private List<Map<String,String>> getData(){

        dataList = new ArrayList<>();
        ArrayList<Note> notes = UserService.getNotes();
        for (int i = 0;i<notes.size();i++) {
            Map<String, String> map = new HashMap<>();
            Note note = notes.get(i);
            map.put("text",note.getContent());
            map.put("time",note.getCreateAt());
            dataList.add(map);
        }
        return dataList;
    }


    private void addListener(){
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NoteEditActivity.class);
                startActivity(intent);
            }
        });

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> noteInfo = (HashMap<String, String>) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),NoteDetailActivity.class);
                intent.putExtra("noteIndex", UserService.findGoalById(Integer.valueOf(noteInfo.get("id"))));
                startActivity(intent);
            }
        });
    }
    public void onResume() {
        super.onResume();
        createListView();
    }
}
