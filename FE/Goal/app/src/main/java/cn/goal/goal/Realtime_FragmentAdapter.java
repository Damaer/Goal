package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/16.
 */

import java.util.List;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class Realtime_FragmentAdapter extends FragmentPagerAdapter {

import java.util.List;
public class Realtime_FragmentAdapter extends FragmentPagerAdapter
{

    private List<Fragment> fragmentList;

    public Realtime_FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
