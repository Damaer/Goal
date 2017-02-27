package cn.goal.goal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherApps.Callback;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.goal.goal.Goal_record_class;
import cn.goal.goal.R;
import cn.goal.goal.services.CommentService;
import cn.goal.goal.services.object.Comment;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.Util;

import static android.R.id.list;
import static android.R.id.startSelectingText;

/**
 * Created by 97617 on 2017/2/22.
 */

public class MyAdapter_for_every_record extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "CMyAdapter_for_every_record";
    private LayoutInflater mInflaer;
    private ArrayList<Comment> list;
    private ImageView[] headphoto;
    private ImageButton[] likeButtons;
    private TextView[] sumOfLikeViews;
    private boolean[] isLike;
    private Context mContext;

    public MyAdapter_for_every_record(Context context, ArrayList<Comment> data) {
        this.list = data;
        mContext = context;
        mInflaer = LayoutInflater.from(context);
        headphoto = new ImageView[data.size()];
        likeButtons = new ImageButton[data.size()];
        isLike = new boolean[data.size()];
        sumOfLikeViews = new TextView[data.size()];
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int position, View view2, ViewGroup parent) {

        ViewHolder holder;
        if (view2 == null) {
            view2 = mInflaer.inflate(R.layout.listview_record_item, null);
            holder = new ViewHolder();
            headphoto[position] = (ImageView) view2.findViewById(R.id.head_photo);
            likeButtons[position] = (ImageButton) view2.findViewById(R.id.like);
            sumOfLikeViews[position] = (TextView) view2.findViewById(R.id.sum_of_like);
            holder.user_name = (TextView) view2.findViewById(R.id.user_name);
            holder.goal_name = (TextView) view2.findViewById(R.id.goal_name);
            holder.content_of_send = (TextView) view2.findViewById(R.id.content_of_record);
            holder.time_of_send = (TextView) view2.findViewById(R.id.time_of_send);
            holder.share = (ImageButton) view2.findViewById(R.id.share);
            holder.reply = (ImageButton) view2.findViewById(R.id.reply);
            holder.sum_of_reply= (TextView) view2.findViewById(R.id.sum_of_reply);
            view2.setTag(holder);
        } else {
            holder = (ViewHolder) view2.getTag();
            headphoto[position] = (ImageView) view2.findViewById(R.id.head_photo);
            likeButtons[position] = (ImageButton) view2.findViewById(R.id.like);
            sumOfLikeViews[position] = (TextView) view2.findViewById(R.id.sum_of_like);
        }

        list.get(position).getUser().setAvatarInterface(new GetBitmapListener(position) {
            @Override
            public void getImg(Bitmap img) {
                super.getImg(img);
                headphoto[position].setImageBitmap(img);
                MyAdapter_for_every_record.this.notifyDataSetChanged();
            }

            @Override
            public void error(String errorInfo) {
                super.error(errorInfo);
                Toast.makeText(mContext, "获取头像失败", Toast.LENGTH_SHORT).show();
            }
        });
        Comment listItem = list.get(position);
        User user = listItem.getUser();
        user.getAvatarBitmap(mContext);
        holder.user_name.setText(user.getUsername());
        holder.goal_name.setText(listItem.goalTitle);
        holder.content_of_send.setText(listItem.getContent());
        holder.time_of_send.setText(Util.dateToString(listItem.getCreateAt()));
        isLike[position] = listItem.getLike().contains(user.get_id());
        likeButtons[position].setBackgroundResource(isLike[position] ? R.mipmap.liked : R.mipmap.like);
        sumOfLikeViews[position].setText(String.valueOf(listItem.getLike().size()));
        holder.share.setBackgroundResource(R.mipmap.share);
        holder.reply.setBackgroundResource(R.mipmap.reply);
        holder.sum_of_reply.setText(String.valueOf(listItem.getNumOfReply()));

        headphoto[position].setOnClickListener(this);
        headphoto[position].setTag(position);
        holder.user_name.setOnClickListener(this);
        holder.user_name.setTag(position);
        holder.goal_name.setOnClickListener(this);
        holder.goal_name.setTag(position);
        holder.content_of_send.setOnClickListener(this);
        holder.content_of_send.setTag(position);
        holder.time_of_send.setOnClickListener(this);
        holder.time_of_send.setTag(position);
        likeButtons[position].setOnClickListener(this);
        likeButtons[position].setTag(position);
        sumOfLikeViews[position].setOnClickListener(this);
        sumOfLikeViews[position].setTag(position);
        holder.share.setOnClickListener(this);
        holder.share.setTag(position);
        holder.reply.setOnClickListener(this);
        holder.reply.setTag(position);
        holder.sum_of_reply.setOnClickListener(this);
        holder.sum_of_reply.setTag(position);
        return view2;
    }

    public class ViewHolder {
        public ImageView headphoto;
        public TextView user_name;
        public TextView goal_name;
        public TextView content_of_send;
        public TextView time_of_send;
        public ImageButton like;
        public TextView sum_of_like;
        public ImageButton share;
        public ImageButton reply;
        public TextView sum_of_reply;
    }

    public void onClick(View v) {
        //得到点击的是哪一个用户的名字
        switch (v.getId()) {
            case R.id.user_name:
            case R.id.head_photo:
                Intent intent=new Intent(mContext,EveryUserActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.goal_name:

                break;
            case R.id.content_of_record:
                Intent intent3=new Intent(mContext,DetailOfRecordActivity.class);
                mContext.startActivity(intent3);
                break;
            case R.id.like:
                new MarkLikeTask((int)v.getTag()).execute();
                break;
            case R.id.share:
                //调用分享
                break;
            case R.id.reply:
                Intent intent2=new Intent(mContext,DetailOfRecordActivity.class);
                mContext.startActivity(intent2);
                break;
            default:
                break;
        }
    }

    class MarkLikeTask extends AsyncTask<Void, Void, String> {
        private LoadingDialog mLoadingDialog;
        private int index;
        private Comment comment;
        private boolean like;

        public MarkLikeTask(int index) {
            super();
            this.index = index;
            this.comment = list.get(index);
            like = !isLike[index];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(mContext);
        }

        @Override
        protected String doInBackground(Void... params) {
            return like ? CommentService.like(comment) : CommentService.unlike(comment);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cancelDialog();
            if (s != null) { // like失败
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, (like ? "" : "取消") + "点赞成功", Toast.LENGTH_SHORT).show();
                isLike[index] = like;
                // 添加下面这句会导致视图不刷新
                sumOfLikeViews[index].setText(
                        String.valueOf(
                                (Integer.valueOf(sumOfLikeViews[index].getText().toString()) + (like ? 1 : -1))
                        )
                );
                sumOfLikeViews[index].postInvalidate();
                likeButtons[index].setBackgroundResource(like ? R.mipmap.liked : R.mipmap.like);
                MyAdapter_for_every_record.this.notifyDataSetChanged();
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
