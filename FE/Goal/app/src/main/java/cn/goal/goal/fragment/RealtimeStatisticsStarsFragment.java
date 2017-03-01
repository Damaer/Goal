package cn.goal.goal.fragment;

/**
 * Created by 97617 on 2017/2/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.goal.goal.R;

public class RealtimeStatisticsStarsFragment extends Fragment
{

    private TextView textview;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_stars, null);
        textview = (TextView) view.findViewById(R.id.textView);
        return view;
    }
}