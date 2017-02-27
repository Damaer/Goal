package cn.goal.goal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import cn.goal.goal.services.RecommendService;
import cn.goal.goal.services.object.Recommend;
import cn.goal.goal.utils.NetWorkUtils;

import java.util.ArrayList;
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
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            new FetchRecommendDataTask().execute();
        } else {
            Toast.makeText(getContext(), "无网络连接", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void createListView(ArrayList<Recommend> recommends){
        if (recommends == null) {
            Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            return ;
        }
        recommendListAdapter = new RecommendAdapter(getActivity(), recommends);

        recommendListVIew.setAdapter(recommendListAdapter);
    }

    class FetchRecommendDataTask extends AsyncTask<Void, Void, ArrayList<Recommend>> {
        private LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(getContext());
        }

        @Override
        protected ArrayList<Recommend> doInBackground(Void... params) {
            return RecommendService.getRecommend();
        }

        @Override
        protected void onPostExecute(ArrayList<Recommend> recommends) {
            super.onPostExecute(recommends);
            cancelDialog();
            createListView(recommends);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null)
                mLoadingDialog.closeDialog();
        }
    }
}
