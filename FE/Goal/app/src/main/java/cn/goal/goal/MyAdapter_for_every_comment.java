package cn.goal.goal;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;

import java.util.ArrayList;

import cn.goal.goal.services.object.Comment;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.Util;

/**
 * Created by 97617 on 2017/2/25.
 */

public class MyAdapter_for_every_comment extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "CMyAdapter_for_every_comment";
    private LayoutInflater mInflaer;
    private ArrayList<Comment> list;
    private Callback mcallback;
    private ImageView[] headphoto;
    private Context mContext;
    private Comment record;
    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author Ivan Xu
     * 2014-11-26
     */
    public interface Callback {
        public void click(View v);
    }
    public MyAdapter_for_every_comment(Context context, ArrayList<Comment> data, Callback callback, Comment record) {
        this.record = record;
        this.list = data;
        mInflaer = LayoutInflater.from(context);
        mcallback=callback;
        headphoto = new ImageView[data.size()];
        mContext = context;
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
            view2 = mInflaer.inflate(R.layout.listview_comment_for_record_item, null);
            holder = new ViewHolder();
            headphoto[position]=(ImageButton)view2.findViewById(R.id.commentuser_photo);
            holder.user_name = (TextView) view2.findViewById(R.id.user_reply);
            holder.replyed_name = (TextView) view2.findViewById(R.id.user_replyed);
            holder.comment_content = (TextView) view2.findViewById(R.id.content_of_comment);
            holder.comment_time = (TextView) view2.findViewById(R.id.time_of_sendcomment);
            view2.setTag(holder);
        } else {
            holder = (ViewHolder) view2.getTag();
            headphoto[position]=(ImageButton)view2.findViewById(R.id.commentuser_photo);
        }
        Comment comment = list.get(position);
        User user = comment.getUser();
        user.setAvatarInterface(new GetBitmapListener(position) {
            @Override
            public void getImg(Bitmap img) {
                super.getImg(img);
                headphoto[tag].setImageBitmap(img);
                MyAdapter_for_every_comment.this.notifyDataSetChanged();
            }

            @Override
            public void error(String errorInfo) {
                super.error(errorInfo);
                Toast.makeText(mContext, "获取头像失败", Toast.LENGTH_SHORT).show();
            }
        });
        user.getAvatarBitmap(mContext);
        holder.user_name.setText(user.getUsername());
        holder.replyed_name.setText(record.getUser().getUsername());
        holder.comment_content.setText(comment.getContent());
        holder.comment_time.setText(Util.dateToString(comment.getCreateAt()));

        headphoto[position].setOnClickListener(this);
        headphoto[position].setTag(position);
        holder.user_name.setOnClickListener(this);
        holder.user_name.setTag(position);
        holder.replyed_name.setOnClickListener(this);
        holder.replyed_name.setTag(position);
        holder.comment_content.setOnClickListener(this);
        holder.comment_content.setTag(position);
        holder.comment_time.setOnClickListener(this);
        holder.comment_time.setTag(position);
        return view2;
    }

    public class ViewHolder {
        public TextView user_name;
        public TextView replyed_name;
        public TextView comment_content;
        public TextView comment_time;
    }

    public void onClick(View v) {
        mcallback.click(v);
    }
}

