package cn.goal.goal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.goal.goal.activity.EveryGoalActivity;
import cn.goal.goal.activity.EveryUserActivity;
import cn.goal.goal.GetBitmapListener;
import cn.goal.goal.dialog.LoadingDialog;
import cn.goal.goal.R;
import cn.goal.goal.services.FollowService;
import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.Recommend;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.Meta;
import cn.goal.goal.utils.NetWorkUtils;
import cn.goal.goal.utils.Util;

import java.util.ArrayList;

/**
 * Created by Tommy on 2017/2/26.
 */

public class RecommendAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<Recommend> mRecommendArrayList;
    private Activity mActivity;
    private Button[] followButtons;
    private ImageView[] portraitImgs;

    private int len;

    public RecommendAdapter(Activity activity,ArrayList<Recommend> recommendArrrayList){
        this.mActivity = activity;
        this.mRecommendArrayList = recommendArrrayList;

        len = recommendArrrayList.size();
        followButtons = new Button[len];
        portraitImgs = new ImageView[len];
    }

    @Override
    public int getCount() {
        return mRecommendArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        TextView userName;
        TextView shareTime;
        TextView shareContent;
        TextView fromWhichGoal;
        Recommend recommend;

        recommend = mRecommendArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) mActivity.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.recommend_list_item, null);

        portraitImgs[position] = (ImageView) v.findViewById(R.id.portrait_img);
        userName = (TextView) v.findViewById(R.id.name);
        shareTime = (TextView) v.findViewById(R.id.share_time);
        shareContent = (TextView) v.findViewById(R.id.share_text);
        fromWhichGoal = (TextView) v.findViewById(R.id.from_goal);
        followButtons[position] = (Button) v.findViewById(R.id.follow_button);
        followButtons[position].setTag(position);

        recommend.getUser().setAvatarInterface(new GetBitmapListener(position) {
            @Override
            public void getImg(Bitmap img) {
                portraitImgs[tag].setImageBitmap(img);
                RecommendAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void error(String errorInfo) {
                Toast.makeText(mActivity, "获取头像失败", Toast.LENGTH_SHORT).show();
            }
        });
        recommend.getUser().getAvatarBitmap(mActivity);
        portraitImgs[position].setOnClickListener(this);
        portraitImgs[position].setTag(position);
        portraitImgs[position].setTag(position);
        userName.setText(recommend.getUser().getUsername());
        userName.setOnClickListener(this);
        userName.setTag(position);
        shareTime.setText(Util.dateToString(recommend.getCreateAt()));
        shareContent.setText(recommend.getContent());
        shareContent.setOnClickListener(this);
        shareContent.setTag(position);
        fromWhichGoal.setText(recommend.getGoal().getTitle());
        fromWhichGoal.setOnClickListener(this);
        fromWhichGoal.setTag(position);

        ArrayList<String> followers = recommend.getFollower();
        if (followers.contains(UserService.getUserInfo().get_id())) { // 当前用户在该用户的关注列表中
            followButtons[position].setText("已关注");
        } else {
            followButtons[position].setText("关注");
        }
        followButtons[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkUtils.isNetworkConnected(mActivity)) {
                    new FollowTask((int)v.getTag()).execute();
                } else {
                    Toast.makeText(mActivity, "当前环境无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void addFollow(String author, String _id) {
        Recommend recommend;
        for (int i = 0; i < len; ++i) {
            if (followButtons[i] != null) {
                recommend = mRecommendArrayList.get(i);
                if (author.equals(recommend.getUser().get_id())) {
                    recommend.getFollower().add(_id);
                    followButtons[i].setText("已关注");
                }
            }
        }
    }

    private void removeFollow(String author, String _id) {
        Recommend recommend;
        for (int i = 0; i < len; ++i) {
            if (followButtons[i] != null) {
                recommend = mRecommendArrayList.get(i);
                if (author.equals(recommend.getUser().get_id())) {
                    recommend.getFollower().remove(_id);
                    followButtons[i].setText("关注");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Recommend recommend = mRecommendArrayList.get((int) v.getTag());
        switch (v.getId()) {
            case R.id.name:
            case R.id.portrait_img:
                // 访问用户
                Meta.otherUser = recommend.getUser();
                mActivity.startActivity(new Intent(mActivity, EveryUserActivity.class));
                break;
            case R.id.share_text:
            case R.id.from_goal:
                // 访问Goal
                Meta.everyGoal = recommend.getGoal();
                mActivity.startActivity(new Intent(mActivity, EveryGoalActivity.class));
                break;
        }

    }

    private class FollowTask extends AsyncTask<Void, Void, String> {
        String userId = UserService.getUserInfo().get_id(); // 当前用户id
        private User user; // 写评论的人
        private Boolean follow; // 标记是否为关注别人
        private LoadingDialog mLoadingDialog;

        public FollowTask(int index) {
            super();
            Recommend recommend = mRecommendArrayList.get(index);
            this.user = recommend.getUser();
            this.follow = followButtons[index].getText().equals("关注");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(mActivity);
        }

        @Override
        protected String doInBackground(Void... params) {
            return follow ? FollowService.followUser(user) : FollowService.unfollowUser(user);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s == null) {
                if (follow) {
                    addFollow(user.get_id(), userId);
                } else {
                    removeFollow(user.get_id(), userId);
                }
            } else {
                Toast.makeText(mActivity, (follow ? "关注" : "取消关注") + "失败", Toast.LENGTH_SHORT).show();
            }
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
