package cn.goal.goal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.utils.Util;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalListFragment extends Fragment {
    private Boolean finished; // 标记是否显示目标已完成, 0表示未完成
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
    private void createListView(ArrayList<GoalUserMap> goals) {
        data = new ArrayList<>();
        if (goals == null) {
            goals = new ArrayList<>();
        }
        for (int i = 0; i < goals.size(); ++i) {
            HashMap<String, String> itemData = new HashMap<>();
            GoalUserMap goal = goals.get(i);
            if (goal.getFinish() == finished) {
                itemData.put("index", GoalUserMapService.getGoalIndex(goal));
                itemData.put("title", goal.getGoal().getTitle());
                itemData.put("content", goal.getGoal().getContent());
                itemData.put("createAt", Util.dateToString(goal.getCreateAt()));
                itemData.put("finished", finished.toString());
                itemData.put("begin", Util.dateToString(goal.getBegin()));
                itemData.put("plan", Util.dateToString(goal.getPlan()));
                itemData.put("end", Util.dateToString(goal.getEnd()));
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> goalInfo = (HashMap<String, String>) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), GoalDetailActivity.class);
                intent.putExtra("goalIndex", goalInfo.get("index"));

                startActivity(intent);
            }
        });
    }

    public void showpopwindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        new FetchGoalsDataTask().execute();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新goals数据
        new FetchGoalsDataTask().execute();
    }

    class FetchGoalsDataTask extends AsyncTask<Void, Void, ArrayList<GoalUserMap>> {
        private LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(getContext());
        }

        @Override
        protected ArrayList<GoalUserMap> doInBackground(Void... params) {
            return GoalUserMapService.getGoals();
        }

        @Override
        protected void onPostExecute(ArrayList<GoalUserMap> s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s == null) { // 获取失败则提示用户是否重新获取
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("获取目标信息失败，是否重新获取?");
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new FetchGoalsDataTask().execute();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else { // 获取成功
                createListView(s);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }

}
