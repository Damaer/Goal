package cn.goal.goal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Tommy on 2017/2/26.
 */

public class RecommendAdapter extends BaseAdapter {
    private ArrayList<Map<String,Object>> recommendArrayList;
    private Activity activity;
    private boolean follow;
    private Button followButton;
    public RecommendAdapter(Activity activity,ArrayList<Map<String,Object>>recommendArrrayList){
        this.activity = activity;
        this.recommendArrayList = recommendArrrayList;
        follow = false;
    }

    @Override
    public int getCount() {
        return recommendArrayList.size();
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
        LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.recommend_list_item, null);

        followButton = (Button) v.findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(follow == false){
                    followButton.setBackgroundColor(Integer.parseInt("#66BB6A"));
                    follow = true;
                }else{
                    followButton.setBackgroundColor(Integer.parseInt("#E8F5E9"));
                    followButton.setTextColor(Integer.parseInt("#E8F5E9"));
                    followButton.setText("已关注");
                }

            }
        });
            return v;

    }
}
