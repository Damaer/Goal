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
        addListener();
        return view;
    }

    private void createListView(){

        recommendListAdapter = new RecommendAdapter(getActivity(),getData());
//        recommendListAdapter = new SimpleAdapter(getContext(),
//                getData(), R.layout.recommend_list_item,new String[] {"portrait","name","follow_number",
//                "follow_button","share_text","like_button","share_button","like_number","follow_number","follow_add_button"},
//                new int[] {R.id.portrait_img,R.id.name,R.id.follow_number,
//                        R.id.follow_button,R.id.share_text,R.id.like_button,R.id.share_button,
//                        R.id.like_number,R.id.follow_number});

        recommendListVIew.setAdapter(recommendListAdapter);
    }

    private ArrayList<Map<String,Object>> getData(){

        dataList = new ArrayList<>();
        for (int i = 0;i<20;i++) {
            Map<String, Object> map = new HashMap<>();
            /*
            * new 一个Recommend，然后添加到map里面去,把用户名称，头像，都放进去；
             */
           // Recommend data = new Recommend();
            map.put("name","name"+i);//getUserName获取用户姓名
            map.put("portrait",R.mipmap.ic_launcher);//getUserImg获取用户头像
            map.put("follow_number","2017-2-2");//getCreateAt获取用户时间
            map.put("follow_button","关注");
            map.put("follow_add_button",R.drawable.ic_add_white_24dp);
            map.put("share_text","你熟悉爱辜负大家发的的李开复哈大姐夫监控卡两地分居漏电开关暗恋看到过暗地里看风景暗恋的看法");//getContent();获取内容
            dataList.add(map);
        }
        return dataList;
    }
    private  void  addListener(){
//        followButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(follow == false){
//                    followButton.setBackgroundColor(Integer.parseInt("#66BB6A"));
//                    follow = true;
//                }else{
//                    followButton.setBackgroundColor(Integer.parseInt("#E8F5E9"));
//                    followButton.setTextColor(Integer.parseInt("#E8F5E9"));
//                    followButton.setText("已关注");
//                }
//            }
//        });
    }

}
