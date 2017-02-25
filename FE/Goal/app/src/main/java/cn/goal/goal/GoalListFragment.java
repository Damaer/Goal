package cn.goal.goal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Goal;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalListFragment extends Fragment {
    private int finished; // 标记是否显示目标已完成, 0表示未完成
    private View mView;
    private ListView mListView;

    ArrayList<Map<String, String>> data;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.containsKey("data")) {
            finished = args.getBoolean("data") ? 1 : 0;
        }
    }
    private void createListView() {
        data = new ArrayList<>();
        ArrayList<Goal> goals = UserService.getGoals();
        if (goals == null) {
            goals = new ArrayList<>();
        }
        for (int i = 0; i < goals.size(); ++i) {
            HashMap<String, String> itemData = new HashMap<>();
            Goal goal = goals.get(i);
            if (goal.getFinished() == finished) {
                itemData.put("id", String.valueOf(goal.getId()));
                itemData.put("title", goal.getTitle());
                itemData.put("content", goal.getContent());
                itemData.put("createAt", goal.getCreateAt());
                itemData.put("finished", finished == 0 ? "false" : "true");
                itemData.put("begin", goal.getBegin());
                itemData.put("plan", goal.getPlan());
                itemData.put("end", goal.getEnd());
                data.add(itemData);
            }
        }

        mListView.setAdapter(new SimpleAdapter(getContext(), data, R.layout.goal_item,
                new String[]{"title", "content", "createAt"},
                new int[]{R.id.title, R.id.content, R.id.createAt}));
        //长按跳出删除弹窗
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showpopwindow();
                return false;
            }
        });

}
    public void showpopwindow()
    {
                             /*
                  *这里使用了 android.support.v7.app.AlertDialog.Builder
                  *可以直接在头部写 import android.support.v7.app.AlertDialog
                  *那么下面就可以写成 AlertDialog.Builder
                  */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setMessage("是否要删除该目标");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
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
                Intent intent = new Intent(getActivity(), EveryGoalActivity.class);
                String goalid = goalInfo.get("id");
                int testid = UserService.findGoalById(Integer.valueOf(goalInfo.get("id")));
                intent.putExtra("goalIndex", UserService.findGoalById(Integer.valueOf(goalInfo.get("id"))));

                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新goals数据
        createListView();
    }

}
