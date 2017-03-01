package cn.goal.goal.adapter;

/**
 * Created by 97617 on 2017/2/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class RealtimeFragmentAdapter extends FragmentPagerAdapter {
    
    private List<Fragment> fragmentList;

    public RealtimeFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragmentList.size();
    }
}
