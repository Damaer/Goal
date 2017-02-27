package cn.goal.goal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Date;

import cn.goal.goal.services.CommentService;
import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.Comment;
import cn.goal.goal.services.object.GoalFinished;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.utils.Meta;
import cn.goal.goal.utils.MylistView_for_goal_record;
import cn.goal.goal.utils.NetWorkUtils;

public class EveryGoalActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener {
    private ImageButton comeback;
    private ImageButton write;
    private MylistView_for_goal_record listView;
    private ImageButton go_goal;
    private ScrollView scrollView;
    private PopupMenu mPopupMenu;
    private String goalIndex;
    private GoalUserMap goal; // 存放goal信息
    private TextView titleView;

    private boolean isJoin; // 判断用户是否添加当前目标
    private boolean finishedToday; // 标记当前目标今日是否已完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_goal);
        // 传入数据不正确
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (Meta.everyGoal != null) {
                isJoin = false;
                goal = new GoalUserMap(Meta.everyGoal, null, null, null, null, null, null, null, null, 0);
            } else {
                Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
                finish();
                return ;
            }
        } else {
            goalIndex = getIntent().getExtras().getString("goalIndex");
            goal = GoalUserMapService.getGoal(goalIndex);
        }

        listView= (MylistView_for_goal_record)findViewById(R.id.every_record_listview);
        titleView = (TextView)findViewById(R.id.title);
        titleView.setText(goal.getGoal().getTitle()); // 设置标题
        comeback=(ImageButton)findViewById(R.id.comeback_from_goal);
        write= (ImageButton) findViewById(R.id.write);
        go_goal= (ImageButton) findViewById(R.id.go_to_goal);
        //右上角的菜单选择
        mPopupMenu = new PopupMenu(this,write);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.write_and_setting, mPopupMenu.getMenu());
        mPopupMenu.getMenu().getItem(2).setEnabled(goal.getFinish() == null || goal.getFinish() == false);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if(title.equals("记录今日"))
                {
                    record();
                }
                if(title.equals("删除目标"))
                {
                    showDialog_delete();
                }
                if(title.equals("完成目标"))
                {
                    showDialog_finish();
                }
                return true;
            }
        });
        write.setVisibility(View.INVISIBLE);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.show();
            }
        });
        init_listener();

        scrollView = (ScrollView)findViewById(R.id.scrollview);
        scrollView.smoothScrollTo(0,0);//滚动条一开始移动到顶端

        // 获取所需目标信息
        if (NetWorkUtils.isNetworkConnected(this)) {
            new InitDataTask().execute();
            new FetchCommentsTask(goal).execute();
        } else {
            Toast.makeText(this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    public void record()
    {
        Intent intent = new Intent(this,GoalRecordActivity.class);
        intent.putExtra("goalIndex", goalIndex);
        startActivityForResult(intent, GoalRecordActivity.REQUEST_RECORD);
    }

    /**
     * 初始化监听事件
     */
   public void init_listener()
    {
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        go_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isJoin) {
                    showDialog2();
                } else if (!finishedToday && (goal.getFinish() == null || goal.getFinish() == false)) {
                    showDialog1();
                }
            }
        });
    }
    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog1() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("朝着目标前进一小步");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetWorkUtils.isNetworkConnected(EveryGoalActivity.this)) {
                    new MarkGoalFinishedTodayTask(goal).execute();
                } else {
                    Toast.makeText(EveryGoalActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showDialog2() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("是否加入这个目标？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new AddGoalTask().execute();
            }
        });
        builder.show();
    }
    private void showDialog_delete() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("是否删除这个目标？");
        builder.setMessage("目标设立不易，且坚持且珍惜");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetWorkUtils.isNetworkConnected(EveryGoalActivity.this)) {
                    new DeleteGoalTask(goal).execute();
                } else {
                    Toast.makeText(EveryGoalActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    private void showDialog_finish() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("已经完成这个目标？");
        builder.setMessage("做最好的自己，一步一步，一直在路上，加油！");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetWorkUtils.isNetworkConnected(EveryGoalActivity.this)) {
                    new MarkGoalFinishedTask(goal).execute();
                } else {
                    Toast.makeText(EveryGoalActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int position) {
        switch (position)
        {
            case SCROLL_STATE_FLING:
                //下拉添加数据源
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int position, int i1, int i2) {

    }
    /**
      * 响应ListView中item的点击事件
      */
   @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
       Intent intent=new Intent(EveryGoalActivity.this,DetailOfRecordActivity.class);
       intent.putExtra("commentIndex", position);
       startActivity(intent);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoalRecordActivity.REQUEST_RECORD) {
            if (resultCode == RESULT_OK) {
                // 更新信息
                if (NetWorkUtils.isNetworkConnected(EveryGoalActivity.this)) {
                    new FetchCommentsTask(goal).execute();
                }
            }
        }
    }

    public void renderCommends(ArrayList<Comment> comments) {
        // 给所有comment添加上goal信息
        for (int i = 0; i < comments.size(); ++i) {
            comments.get(i).goal = goal.getGoal();
        }
        MyAdapter_for_every_record myadater=new MyAdapter_for_every_record(this,comments);
        listView.setAdapter(myadater);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    class InitDataTask extends AsyncTask<Void, Void, GoalFinished> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected GoalFinished doInBackground(Void... params) {
            // 判断当前目标是否被当前用户添加
            isJoin = false;
            ArrayList<GoalUserMap> goalUserMaps = GoalUserMapService.getGoals();
            for (int i = 0; i < goalUserMaps.size(); ++i) {
                GoalUserMap goalUserMap = goalUserMaps.get(i);
                if (goalUserMap.getGoal().get_id().equals(goal.getGoal().get_id())) {
                    isJoin = true;
                }
            }
            return isJoin ? GoalUserMapService.getGoalsFinished() : null;
        }

        @Override
        protected void onPostExecute(GoalFinished goalFinisheds) {
            super.onPostExecute(goalFinisheds);
            cancelDialog();
            if (!isJoin) { // 未添加当前目标
                go_goal.setImageResource(R.mipmap.add);
            } else if (goalFinisheds == null) {
                Toast.makeText(EveryGoalActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
            } else {
                write.setVisibility(View.VISIBLE);
                // 判断当前目标今日是否已完成
                boolean flag = false;
                for (String goalId : goalFinisheds.getGoal()) {
                    if (goalId.equals(goal.getGoal().get_id())) {
                        flag = true;
                        break;
                    }
                }

                finishedToday = flag;
                go_goal.setImageResource(finishedToday ? R.mipmap.go2 : R.mipmap.go1);
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

    class DeleteGoalTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;
        private GoalUserMap goal;

        public DeleteGoalTask(GoalUserMap goal) {
            super();
            this.goal = goal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return GoalUserMapService.deleteGoal(goal);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 删除失败
                Toast.makeText(EveryGoalActivity.this, s, Toast.LENGTH_SHORT).show();
            } else { // 删除成功
                finish();
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

    class MarkGoalFinishedTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;
        private GoalUserMap goal;

        public MarkGoalFinishedTask(GoalUserMap goal) {
            super();
            this.goal = goal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return GoalUserMapService.markFinished(goal);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 标记完成失败
                Toast.makeText(EveryGoalActivity.this, s, Toast.LENGTH_SHORT).show();
            } else { // 标记完成成功
                mPopupMenu.getMenu().getItem(2).setEnabled(false);
                Toast.makeText(EveryGoalActivity.this, "目标已完成！", Toast.LENGTH_SHORT).show();
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

    class MarkGoalFinishedTodayTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;
        private GoalUserMap goal;

        public MarkGoalFinishedTodayTask(GoalUserMap goal) {
            super();
            this.goal = goal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return GoalUserMapService.markGoalFinishedToday(goal);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 标记完成失败
                Toast.makeText(EveryGoalActivity.this, s, Toast.LENGTH_SHORT).show();
            } else { // 标记完成成功
                finishedToday = true;
                go_goal.setImageResource(R.mipmap.go2);
                Toast.makeText(EveryGoalActivity.this, "记录成功！", Toast.LENGTH_SHORT).show();
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

    class FetchCommentsTask extends AsyncTask<Void, Void, ArrayList<Comment>> {
        private LoadingDialog mLoadingDialog;
        private GoalUserMap goal;

        public FetchCommentsTask(GoalUserMap goal) {
            super();
            this.goal = goal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected ArrayList<Comment> doInBackground(Void... params) {
            return CommentService.getCommentOfGoal(goal.getGoal());
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s == null) { // 获取评论失败
                Toast.makeText(EveryGoalActivity.this, "获取记录列表失败", Toast.LENGTH_SHORT).show();
            } else {
                renderCommends(s);
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

    class AddGoalTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryGoalActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return GoalUserMapService.addGoal(goal.getGoal(), new Date(), new Date(), true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // 添加失败
                Toast.makeText(EveryGoalActivity.this, "添加失败，请重新尝试", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EveryGoalActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                isJoin = true;
                write.setVisibility(View.VISIBLE);
                go_goal.setImageResource(R.mipmap.go1);
                new InitDataTask().execute(); // 刷新数据
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
