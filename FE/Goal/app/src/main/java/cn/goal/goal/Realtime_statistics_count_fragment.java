package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/16.
 */
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cn.goal.goal.services.AnalyseService;
import cn.goal.goal.services.object.Analyse;
import cn.goal.goal.services.object.GoalsFinishedRecord;
import cn.goal.goal.utils.NetWorkUtils;

import static cn.goal.goal.R.id;
import static cn.goal.goal.R.layout;

public class Realtime_statistics_count_fragment extends Fragment {
    private TextView time;
    private TextView createdView;
    private TextView doingView;
    private TextView finishedView;
    private TextView unfinishedView;
    private TextView focusTimeView;
    private View view;
    private ArrayList<ImageView> recordsView;

    private int[] color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // TODO Auto-generated method stub
        view = inflater.inflate(layout.fragement_count_and_analyse, null);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        time=(TextView) view.findViewById(R.id.time);
        time.setText(year+"年"+month+"月");
        // 获取day1-day32
        int day1Id = R.id.day1;
        recordsView = new ArrayList<>();
        for (int i = 0; i < 32; ++i) {
            recordsView.add((ImageView) view.findViewById(day1Id + i));
        }

        // 获取TextView
        createdView = (TextView) view.findViewById(id.created);
        doingView = (TextView) view.findViewById(id.doing);
        finishedView = (TextView) view.findViewById(id.finished);
        unfinishedView = (TextView) view.findViewById(id.unfinished);
        focusTimeView = (TextView) view.findViewById(id.focusTime);

        // 创建记录梯度颜色
        color = new int[]{Color.rgb(232,245,233), Color.rgb(129,199,132), Color.rgb(102,187,106), Color.rgb(67,160,71), Color.rgb(27,94,32)};

        if (NetWorkUtils.isNetworkConnected(getContext())) {
            new FetchDataTask().execute();
        } else {
            Toast.makeText(getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /**
     * 渲染数据到视图
     * @param analyse
     */
    private void render(Analyse analyse) {
        if (analyse == null) {
            Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        renderGoalsFinishedRecord(analyse.getGoalsFinishedRecord());

        createdView.setText(String.valueOf(analyse.getGoalsCreated()));
        doingView.setText(String.valueOf(analyse.getGoalsDoing()));
        finishedView.setText(String.valueOf(analyse.getGoalsFinished()));
        unfinishedView.setText(String.valueOf(analyse.getGoalsUnfinished()));
        focusTimeView.setText(String.valueOf(analyse.getFocusTime()));
    }

    private void renderGoalsFinishedRecord(ArrayList<GoalsFinishedRecord> goalsFinishedRecords) {
        // 将记录清空
        for (int i = 0; i < 32; ++i) {
            recordsView.get(i).setBackgroundColor(color[0]);
        }

        for (GoalsFinishedRecord goalsFinishedRecord : goalsFinishedRecords) {
            recordsView.get(goalsFinishedRecord.getDate().getDate() - 1).setBackgroundColor(getColor(goalsFinishedRecord.getNumOfGoalFinished()));
        }
    }

    private int getColor(int num) {
        switch (num) {
            case 0:
                return color[0];
            case 1:
                return color[1];
            case 2:
                return color[2];
            case 5:
                return color[3];
            case 8:
                return color[4];
            default:
                return color[0];
        }
    }

    class FetchDataTask extends AsyncTask<Void, Void, Analyse> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(getContext());
        }

        @Override
        protected Analyse doInBackground(Void... params) {
            return AnalyseService.getAnalyseOfCurrentMonth();
        }

        @Override
        protected void onPostExecute(Analyse analyse) {
            super.onPostExecute(analyse);
            cancelDialog();
            render(analyse);
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
