package cn.goal.goal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goal.goal.services.FollowService;
import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.MessageSerivce;
import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.GetBitmapInterface;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.services.object.User;
import cn.goal.goal.services.object.UserInfo;
import cn.goal.goal.utils.Meta;
import cn.goal.goal.utils.MylistView_for_goal;
import cn.goal.goal.utils.NetWorkUtils;
import cn.goal.goal.utils.Util;

import static cn.goal.goal.R.id.listview;

public class EveryUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private TextView goalname;
    private TextView time_of_send;
    private TextView sum_of_people;
    private MylistView_for_goal list;
    private ImageButton comeback;
    private ImageButton setting;
    private ScrollView scrollView;
    private PopupMenu mPopupMenu;
    private EditText content_of_send;
    private Button send_comment;
    private RelativeLayout rl_comment;
    private TextView hide_down;

    private TextView userNameView;
    private ImageView avatarView;
    private TextView focusTimeView;
    private TextView numOfGoalView;
    private TextView numOfFollower;
    private TextView descriptionView;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_user);

        if (Meta.otherUser == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        user = Meta.otherUser;

        initlistener();
        setting= (ImageButton) findViewById(R.id.setting_for_user);

        mPopupMenu = new PopupMenu(this,setting);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.user_setting, mPopupMenu.getMenu());
        if (UserService.getUserInfo().get_id().equals(user.get_id())) {
            setting.setVisibility(View.GONE);
        }
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.show();
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        if(title.equals("发信息"))
                        {
                            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            rl_comment.setVisibility(View.VISIBLE);
                            scrollView.smoothScrollTo(0,0);
                        } else {
                            // 关注当前用户
                            if (NetWorkUtils.isNetworkConnected(EveryUserActivity.this)) {
                                new FollowTask().execute();
                            } else {
                                Toast.makeText(EveryUserActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                });
            }
        });

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发送消息
                if (NetWorkUtils.isNetworkConnected(EveryUserActivity.this)) {
                    new SendMessageTask(content_of_send.getText().toString()).execute();
                } else {
                    Toast.makeText(EveryUserActivity.this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(content_of_send.getWindowToken(), 0);
                rl_comment.setVisibility(View.GONE);
            }
        });
        list= (MylistView_for_goal) findViewById(R.id.listview_of_user_goal);

        if (NetWorkUtils.isNetworkConnected(this)) {
            new GetUserInfoTask().execute();
            new GetUserGoalTask().execute();
        } else {
            Toast.makeText(this, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    public void initlistener()
    {
        goalname= (TextView) findViewById(R.id.goal_name_of_user);
        comeback=(ImageButton) findViewById(R.id.comeback_from_user);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scrollView= (ScrollView) findViewById(R.id.scrollview_for_user);
        scrollView.smoothScrollTo(0,0);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        content_of_send= (EditText) findViewById(R.id.comment_content);
        send_comment= (Button) findViewById(R.id.comment_send_message);
        hide_down= (TextView) findViewById(R.id.hide_down);

        userNameView = (TextView) findViewById(R.id.userName);
        userNameView.setText(user.getUsername());
        avatarView = (ImageView) findViewById(R.id.user_head_photo);
        user.setAvatarInterface(new GetBitmapInterface() {
            @Override
            public void getImg(Bitmap img) {
                avatarView.setImageBitmap(img);
            }

            @Override
            public void error(String errorInfo) {

            }
        });
        user.getAvatarBitmap(this);

        descriptionView = (TextView) findViewById(R.id.word_of_user);
        focusTimeView = (TextView) findViewById(R.id.time_attention);
        numOfGoalView = (TextView) findViewById(R.id.sum_goal);
        numOfFollower = (TextView) findViewById(R.id.payattention_to_her);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, Object> info = (Map<String, Object>) adapterView.getItemAtPosition(i);
        Meta.everyGoal = ((GoalUserMap) info.get("data")).getGoal();
        Intent intent = new Intent(this, EveryGoalActivity.class);
        startActivity(intent);
    }

    private void renderUserInfo(UserInfo userInfo) {
        descriptionView.setText(userInfo.getDescription());
        focusTimeView.setText(String.valueOf(userInfo.getFocusTime()) + "分钟");
        numOfFollower.setText(String.valueOf(userInfo.getFollowers().size()) + "个");
        numOfGoalView.setText(String.valueOf(userInfo.getNumOfGoal()) + "人");

        // 判断用户是否已被当前用户关注
        for (int i = 0; i < userInfo.getFollowers().size(); ++i) {
            User follower = userInfo.getFollowers().get(i);
            if (follower.get_id().equals(user.get_id())) {
                mPopupMenu.getMenu().getItem(0).setTitle("取消关注");
            }
        }
    }

    private void renderGoal(ArrayList<GoalUserMap> goalUserMapArrayList) {
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < goalUserMapArrayList.size(); i++) {
            GoalUserMap goalUserMap = goalUserMapArrayList.get(i);
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("data", goalUserMap);
            listem.put("goalname", goalUserMap.getGoal().getTitle());
            listem.put("time_of_set", Util.dateToString(goalUserMap.getCreateAt()));
            listem.put("sum_of_people",  String.valueOf((goalUserMap.getNumberOfPeople()) + "人"));
            listems.add(listem);
        }
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.listview_goal_of_user, new String[]{"goalname","time_of_set","sum_of_people"}
                ,new int[]{R.id.goal_name_of_user,R.id.time_of_setgoal,R.id.sum_of_people});
        list.setOnItemClickListener(this);
        list.setAdapter(simplead);
    }

    class GetUserInfoTask extends AsyncTask<Void, Void, UserInfo> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryUserActivity.this);
        }

        @Override
        protected UserInfo doInBackground(Void... params) {
            return UserService.getOtherUserInfo(user);
        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            super.onPostExecute(userInfo);
            cancelDialog();
            if (userInfo == null) {
                Toast.makeText(EveryUserActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
            } else {
                renderUserInfo(userInfo);
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

    class GetUserGoalTask extends AsyncTask<Void, Void, ArrayList<GoalUserMap>> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryUserActivity.this);
        }

        @Override
        protected ArrayList<GoalUserMap> doInBackground(Void... params) {
            return GoalUserMapService.getOtherUserGoals(user.get_id());
        }

        @Override
        protected void onPostExecute(ArrayList<GoalUserMap> goalUserMapArrayList) {
            super.onPostExecute(goalUserMapArrayList);
            cancelDialog();
            renderGoal(goalUserMapArrayList);
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

    class FollowTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        private boolean follow;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryUserActivity.this);
            follow = mPopupMenu.getMenu().getItem(0).getTitle().equals("关注");
        }

        @Override
        protected String doInBackground(Void... params) {
            return follow ? FollowService.followUser(user) : FollowService.unfollowUser(user);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err == null) {
                Toast.makeText(EveryUserActivity.this, (follow ? "" : "取消") + "关注成功", Toast.LENGTH_SHORT).show();
                mPopupMenu.getMenu().getItem(0).setTitle(follow ? "取消关注" : "关注");
            } else {
                Toast.makeText(EveryUserActivity.this, err, Toast.LENGTH_SHORT).show();
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

    class SendMessageTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        private String content;

        public SendMessageTask(String content) {
            super();
            this.content = content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(EveryUserActivity.this);
            // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
            InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(content_of_send.getWindowToken(), 0);
            rl_comment.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return MessageSerivce.sendMessage(user,  content);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err != null) {
                Toast.makeText(EveryUserActivity.this, err, Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rl_comment.setVisibility(View.VISIBLE);
                scrollView.smoothScrollTo(0,0);
            } else {
                Toast.makeText(EveryUserActivity.this, "发送消息成功", Toast.LENGTH_SHORT).show();
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
