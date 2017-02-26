package cn.goal.goal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherApps.Callback;
import android.telecom.Call;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.goal.goal.Goal_record_class;
import cn.goal.goal.R;

import static android.R.id.list;
import static android.R.id.startSelectingText;

/**
 * Created by 97617 on 2017/2/22.
 */

public class MyAdapter_for_every_record extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "CMyAdapter_for_every_record";
    private LayoutInflater mInflaer;
    private List<Goal_record_class> list;
    private Callback mcallback;
    /**
           * 自定义接口，用于回调按钮点击事件到Activity
           * @author Ivan Xu
           * 2014-11-26
           */
        public interface Callback {
                public void click(View v);
             }
    public MyAdapter_for_every_record(Context context, List<Goal_record_class> list1, Callback callback) {
        this.list = list1;
        mInflaer = LayoutInflater.from(context);
        mcallback=callback;
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
    public View getView(int position, View view2, ViewGroup parent) {

        ViewHolder holder = null;
        if (view2 == null) {
            view2 = mInflaer.inflate(R.layout.listview_record_item, null);
            holder = new ViewHolder();
            holder.headphoto = (ImageView) view2.findViewById(R.id.head_photo);
            holder.user_name = (TextView) view2.findViewById(R.id.user_name);
            holder.goal_name = (TextView) view2.findViewById(R.id.goal_name);
            holder.content_of_send = (TextView) view2.findViewById(R.id.content_of_record);
            holder.time_of_send = (TextView) view2.findViewById(R.id.time_of_send);
            holder.like = (ImageButton) view2.findViewById(R.id.like);
            holder.sum_of_like = (TextView) view2.findViewById(R.id.sum_of_like);
            holder.share = (ImageButton) view2.findViewById(R.id.share);
            holder.reply = (ImageButton) view2.findViewById(R.id.reply);
            holder.sum_of_reply= (TextView) view2.findViewById(R.id.sum_of_reply);
            view2.setTag(holder);
        } else {
            holder = (ViewHolder) view2.getTag();
        }
        holder.headphoto.setBackgroundResource(list.get(position).getUser_Image());
        holder.user_name.setText(list.get(position).getUser_name());
        holder.goal_name.setText(list.get(position).getGoal_name());
        holder.content_of_send.setText(list.get(position).getUser_record());
        holder.time_of_send.setText(list.get(position).getTime_of_send());
        holder.like.setBackgroundResource(list.get(position).getImageId_btn_like());
        holder.sum_of_like.setText(String.valueOf(list.get(position).getSum_of_like()));
        holder.share.setBackgroundResource(list.get(position).getImageId_btn_share());
        holder.reply.setBackgroundResource(list.get(position).getImageId_btn_reply());
       holder.sum_of_reply.setText(String.valueOf(list.get(position).getSum_of_reply()));
        holder.headphoto.setOnClickListener(this);



        holder.headphoto.setTag(position);
        holder.user_name.setOnClickListener(this);
        holder.user_name.setTag(position);
        holder.goal_name.setOnClickListener(this);
        holder.goal_name.setTag(position);
        holder.content_of_send.setOnClickListener(this);
        holder.content_of_send.setTag(position);
        holder.time_of_send.setOnClickListener(this);
        holder.time_of_send.setTag(position);
        holder.like.setOnClickListener(this);
        holder.like.setTag(position);
        holder.sum_of_like.setOnClickListener(this);
        holder.sum_of_like.setTag(position);
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
                 mcallback.click(v);
            }


}
