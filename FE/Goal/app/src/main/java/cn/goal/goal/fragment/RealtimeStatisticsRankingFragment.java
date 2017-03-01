package cn.goal.goal.fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import cn.goal.goal.activity.EveryUserActivity;
import cn.goal.goal.dialog.LoadingDialog;
import cn.goal.goal.R;
import cn.goal.goal.adapter.RankListAdapter;
import cn.goal.goal.services.AnalyseService;
import cn.goal.goal.services.object.Rank;
import cn.goal.goal.utils.Meta;
import cn.goal.goal.utils.NetWorkUtils;

import java.util.ArrayList;

public class RealtimeStatisticsRankingFragment extends Fragment {
    private ListView listview;
    private View view;
    private ArrayList<Rank> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragement_ranking, null);

        if (NetWorkUtils.isNetworkConnected(getContext())) {
            new FetchRankList().execute();
        } else {
            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void render(ArrayList<Rank> rankArrayList) {
        list = rankArrayList;
        RankListAdapter adapter = new RankListAdapter(getContext(), rankArrayList);
        listview= (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //判断点击了哪一个用户
                Meta.otherUser = list.get(i).getUser();
                Intent intent2=new Intent(getActivity(),EveryUserActivity.class);
                startActivity(intent2);
            }
        });
    }

    class FetchRankList extends AsyncTask<Void, Void, ArrayList<Rank>> {
        private LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(getContext());
        }

        @Override
        protected ArrayList<Rank> doInBackground(Void... params) {
            return AnalyseService.getRankArrayList();
        }

        @Override
        protected void onPostExecute(ArrayList<Rank> s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s == null) { // 获取评论失败
                Toast.makeText(getContext(), "获取记录列表失败", Toast.LENGTH_SHORT).show();
            } else {
                render(s);
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