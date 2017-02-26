package cn.goal.goal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 97617 on 2017/2/26.
 */
import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import java.util.List;
        import cn.goal.goal.R;
        import cn.goal.goal.ContactClass;


        import static android.R.id.list;
        import static android.R.id.startSelectingText;
public class MyAdapterForContact extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "CMyAdapter_for_Contact";
    private LayoutInflater mInflaer;
    private List<ContactClass> list;
    private Callback mcallback;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author Ivan Xu
     *         2014-11-26
     */
    public interface Callback {
        public void click(View v);
    }

    public MyAdapterForContact(Context context, List<ContactClass> list1, Callback callback) {
        this.list = list1;
        mInflaer = LayoutInflater.from(context);
        mcallback = callback;
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
            holder.headphoto_ofcontact= (ImageView) view2.findViewById(R.id.user_head_photo_for_contact);
            holder.username_for_contact=(TextView) view2.findViewById(R.id.username_for_contact);
            holder.content_of_contact = (TextView) view2.findViewById(R.id.content_of_contact);
            holder.time_contact = (TextView) view2.findViewById(R.id.time_of_contact);
            view2.setTag(holder);
        } else {
            holder = (MyAdapterForContact.ViewHolder) view2.getTag();
        }
        holder.headphoto_ofcontact.setBackgroundResource(list.get(position).getheadphoto_for_contact());
        holder.content_of_contact.setText(list.get(position).getContent_of_contact());
        holder.username_for_contact.setText(list.get(position).getUsername_for_contact());
        holder.time_contact.setText(list.get(position).getTime_of_contact());
        holder.headphoto_ofcontact.setOnClickListener(this);

        holder.headphoto_ofcontact.setTag(position);
        holder.username_for_contact.setOnClickListener(this);
        holder.username_for_contact.setTag(position);
        holder.content_of_contact.setOnClickListener(this);
        holder.content_of_contact.setTag(position);
        holder.time_contact.setOnClickListener(this);
        holder.time_contact.setTag(position);
        return view2;
    }

    public class ViewHolder {
        public ImageView headphoto_ofcontact;
        public TextView username_for_contact;
        public TextView content_of_contact;
        public TextView time_contact;
    }
    public void onClick(View v) {
        mcallback.click(v);
    }

}