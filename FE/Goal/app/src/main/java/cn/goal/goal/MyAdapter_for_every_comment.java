package cn.goal.goal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.goal.goal.utils.Comment_for_record;

/**
 * Created by 97617 on 2017/2/25.
 */

public class MyAdapter_for_every_comment extends BaseAdapter implements View.OnClickListener{
    private static final String TAG = "CMyAdapter_for_every_comment";
    private LayoutInflater mInflaer;
    private List<Comment_for_record> list;
    private Callback mcallback;
    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author Ivan Xu
     * 2014-11-26
     */
    public interface Callback {
        public void click(View v);
    }
    public MyAdapter_for_every_comment(Context context, List<Comment_for_record> list1, Callback callback) {
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
            view2 = mInflaer.inflate(R.layout.listview_comment_for_record_item, null);
            holder = new ViewHolder();
            holder.user_photo=(ImageButton)view2.findViewById(R.id.commentuser_photo);
            holder.user_name = (TextView) view2.findViewById(R.id.user_reply);
            holder.replyed_name = (TextView) view2.findViewById(R.id.user_replyed);
            holder.comment_content = (TextView) view2.findViewById(R.id.content_of_comment);
            holder.comment_time = (TextView) view2.findViewById(R.id.time_of_sendcomment);
            view2.setTag(holder);
        } else {
            holder = (ViewHolder) view2.getTag();
        }
        holder.user_photo.setBackgroundResource(list.get(position).getuser_photo());
        holder.user_name.setText(list.get(position).getuser_name());
        holder.replyed_name.setText(list.get(position).getreplyed_name());
        holder.comment_content.setText(list.get(position).getcomment_content());
        holder.comment_time.setText(list.get(position).getcomment_time());
        holder.user_photo.setOnClickListener(this);
        holder.user_photo.setTag(position);
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
        public ImageButton user_photo;
        public TextView user_name;
        public TextView replyed_name;
        public TextView comment_content;
        public TextView comment_time;
    }

    public void onClick(View v) {
        mcallback.click(v);
    }


}

