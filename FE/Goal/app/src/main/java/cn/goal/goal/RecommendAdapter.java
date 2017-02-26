package cn.goal.goal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView portraitImg;
    private TextView userName;
    private TextView shareTime;
    private TextView shareContent;
    private ImageButton likeButton;
    private ImageButton shareButton;
    private TextView fromWhichGoal;
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

        portraitImg = (ImageView)v.findViewById(R.id.portrait_img);
        userName  = (TextView)v.findViewById(R.id.name);
        shareTime =(TextView)v.findViewById(R.id.share_time);
        shareContent = (TextView)v.findViewById(R.id.share_text);


        portraitImg.setImageResource(R.mipmap.ic_launcher);
        userName.setText("yonghuming");
        shareTime.setText("2017-2-22");
        shareContent.setText("j;laksdfjeaiofj;asdlkgj;adkfjd;aslkjgwiegja;dlskgj;adlkjfa;dklgj");


        followButton = (Button) v.findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"已关注",Toast.LENGTH_SHORT).show();
            }
        });

           return v;

    }
}
