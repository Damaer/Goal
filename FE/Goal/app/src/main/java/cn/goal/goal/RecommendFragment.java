package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tommy on 2017/2/24.
 */

public class RecommendFragment extends Fragment {
    private View view;
    private ListView recommendListVIew;
    private RecommendAdapter recommendListAdapter;
    private ArrayList<Map<String,Object>> dataList;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ){
            view = inflater.inflate(R.layout.recommend_list,container,false);
        }

        recommendListVIew = (ListView) view.findViewById(R.id.recommend_listView);
        createListView();
        return view;
    }

    private void createListView(){

        recommendListAdapter = new RecommendAdapter(getActivity(),getData());

        recommendListVIew.setAdapter(recommendListAdapter);
    }

    private ArrayList<Map<String,Object>> getData(){

        dataList = new ArrayList<>();
        for (int i = 0;i<20;i++) {
            Map<String, Object> map = new HashMap<>();

            /*
            * 个人认为我new 一个Recommend，然后添加到map里面去,把用户名称，头像，都放进去；然后到Adapter类里面去get会不会比较好？
             */
           // Recommend data = new Recommend();
            map.put("name","name"+i);//getUserName获取用户姓名
            map.put("portrait",R.mipmap.ic_launcher);//getUserImg获取用户头像
            map.put("follow_number","2017-2-2");//getCreateAt获取用户时间
            map.put("follow_button","关注");
            map.put("share_text","你熟悉爱辜负大家发的的李开复哈大姐夫监控卡两地分居漏电开关暗恋看到过暗地里看风景暗恋的看法");//getContent();获取内容
            dataList.add(map);
        }
        return dataList;
    }

}
