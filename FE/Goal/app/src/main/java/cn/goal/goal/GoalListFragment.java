package cn.goal.goal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalListFragment extends Fragment {
    private Boolean finished; // 标记是否显示目标已完成
    private View mView;
    private ListView mListView;

    ArrayList<Map<String, String>> data;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.containsKey("data")) {
            finished = args.getBoolean("data");
        }
    }

    private void createListView() {
        data = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            HashMap<String, String> itemData = new HashMap<>();
            itemData.put("title", "目标标题" + i);
            itemData.put("content", "目标内容目标内容目标内容目标内容目标内容目标内容");
            itemData.put("createAt", "2015-2-1");
            itemData.put("finished", finished ? "true" : "false");
            itemData.put("begin", "2016-1-1");
            itemData.put("plan", "2016-1-3");
            itemData.put("end", "2016-1-2");
            data.add(itemData);
        }

        mListView.setAdapter(new SimpleAdapter(getContext(), data, R.layout.goal_item,
                new String[]{"title", "content", "createAt"},
                new int[]{R.id.title, R.id.content, R.id.createAt}));
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.goal_list, container, false);
        mListView = (ListView) mView.findViewById(R.id.goal_list_view);
        createListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> goalInfo = (HashMap<String, String>) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), GoalDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", goalInfo.get("title"));
                bundle.putString("content", goalInfo.get("content"));
                bundle.putString("begin", goalInfo.get("begin"));
                bundle.putString("plan", goalInfo.get("plan"));
                bundle.putString("end", goalInfo.get("end"));
                bundle.putString("createAt", goalInfo.get("createAt"));
                bundle.putBoolean("finished", goalInfo.get("finished").equals("true"));
                intent.putExtra("data", bundle);

                startActivity(intent);
            }
        });

        return mView;
    }
}
