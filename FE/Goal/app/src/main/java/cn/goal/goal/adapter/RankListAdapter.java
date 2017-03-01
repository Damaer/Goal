package cn.goal.goal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goal.goal.GetBitmapListener;
import cn.goal.goal.R;
import cn.goal.goal.services.object.Rank;
import cn.goal.goal.services.object.User;

import java.util.ArrayList;

/**
 * Created by chenlin on 28/02/2017.
 */
public class RankListAdapter extends BaseAdapter {
    private ArrayList<Rank> list;
    private LayoutInflater mInflaer;
    private Context mContext;

    private ImageView[] avatarArrayView;

    public RankListAdapter(Context context, ArrayList<Rank> data) {
        super();
        mInflaer = LayoutInflater.from(context);
        mContext = context;
        list = data;
        avatarArrayView = new ImageView[data.size()];
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflaer.inflate(R.layout.listview_ranking_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.rankingView = (TextView) convertView.findViewById(R.id.ranking);
            avatarArrayView[position] = (ImageView) convertView.findViewById(R.id.user_photo);
            holder.usernameView = (TextView) convertView.findViewById(R.id.user_name);
            holder.goalValueView = (TextView) convertView.findViewById(R.id.goal_value);
        } else {
            holder = (ViewHolder) convertView.getTag();
            RankListAdapter.this.notifyDataSetChanged();
        }

        Rank rank = list.get(position);
        User user = rank.getUser();
        user.setAvatarInterface(new GetBitmapListener(position) {
            @Override
            public void getImg(Bitmap img) {
                avatarArrayView[tag].setImageBitmap(img);
            }
        });
        user.getAvatarBitmap(mContext);
        holder.rankingView.setText(String.valueOf(position + 1));
        holder.usernameView.setText(user.getUsername());
        holder.goalValueView.setText("专注 " + String.valueOf(rank.getFocusTime()) + " 分钟");

        return convertView;
    }

    public class ViewHolder {
        public TextView rankingView;
        public TextView usernameView;
        public TextView goalValueView;
    }
}
