package cn.goal.goal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 97617 on 2017/2/26.
 */
import cn.goal.goal.GetBitmapListener;
import cn.goal.goal.R;
import cn.goal.goal.Services.object.Message;
import cn.goal.goal.Services.object.User;
import cn.goal.goal.Utils.Util;

public class MyAdapterForContact extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "CMyAdapter_for_Contact";
    private LayoutInflater mInflaer;
    private ArrayList<Message> list;
    private Callback mcallback;
    private Context mContext;
    private ImageView[] avatarArrayView;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author Ivan Xu
     *         2014-11-26
     */
    public interface Callback {
        public void click(View v);
    }

    public MyAdapterForContact(Context context, ArrayList<Message> data, Callback callback) {
        this.list = data;
        mContext = context;
        mInflaer = LayoutInflater.from(context);
        mcallback = callback;
        avatarArrayView = new ImageView[data.size()];
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
            view2 = mInflaer.inflate(R.layout.listview_contact_item, null);
            holder = new ViewHolder();
            avatarArrayView[position] = (ImageView) view2.findViewById(R.id.user_head_photo_for_contact);
            holder.username_for_contact=(TextView) view2.findViewById(R.id.username_for_contact);
            holder.content_of_contact = (TextView) view2.findViewById(R.id.content_of_contact);
            holder.time_contact = (TextView) view2.findViewById(R.id.time_of_contact);
            view2.setTag(holder);
        } else {
            holder = (MyAdapterForContact.ViewHolder) view2.getTag();
            avatarArrayView[position] = (ImageView) view2.findViewById(R.id.user_head_photo_for_contact);
        }
        Message message = list.get(position);
        User sender = message.getSender();
        sender.setAvatarInterface(new GetBitmapListener(position) {
            @Override
            public void getImg(Bitmap img) {
                super.getImg(img);
                avatarArrayView[tag].setImageBitmap(img);
            }
        });
        sender.getAvatarBitmap(mContext);
        holder.content_of_contact.setText(message.getContent());
        holder.username_for_contact.setText(sender.getUsername());
        holder.time_contact.setText(Util.dateToString(message.getCreateAt()));
        avatarArrayView[position].setOnClickListener(this);
        holder.content_of_contact.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("");
                builder.setMessage("删除该信息？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //点击确定之后跳转到
                    }
                });
                builder.show();
                return true;
            }
        });
        avatarArrayView[position].setTag(position);
        holder.username_for_contact.setOnClickListener(this);
        holder.username_for_contact.setTag(position);
        holder.content_of_contact.setOnClickListener(this);
        holder.content_of_contact.setTag(position);
        holder.time_contact.setOnClickListener(this);
        holder.time_contact.setTag(position);
        return view2;
    }

    public class ViewHolder {
        public TextView username_for_contact;
        public TextView content_of_contact;
        public TextView time_contact;
    }
    public void onClick(View v) {
        mcallback.click(v);
    }


}