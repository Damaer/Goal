package cn.goal.goal;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Realtime_statistics_ranking_Fragment extends Fragment {

    private ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragement_ranking, null);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            User_ranking one = new User_ranking(i+1,R.drawable.word,"yonghuming",1.00f);
            listem.put("rank",one.getrank());
            listem.put("head", one.getImageId());
            listem.put("name", "用户名"+i);
            listem.put("value",one.getGoalvalue());
            listems.add(listem);
        }
        SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                R.layout.listview_ranking_item, new String[]{"rank", "head", "name","value"}
                ,new int[]{R.id.ranking,R.id.user_photo,R.id.user_name,R.id.goal_value});
        listview= (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(simplead);
        return view;
    }
}