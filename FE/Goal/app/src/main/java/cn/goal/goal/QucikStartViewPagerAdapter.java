package cn.goal.goal;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 97617 on 2017/5/10.
 */

public class QucikStartViewPagerAdapter extends PagerAdapter {
    private List<View>mlistviews;
    public QucikStartViewPagerAdapter(List<View>mlistviews){
        this.mlistviews=mlistviews;
    }
    @Override
    public void  destroyItem(ViewGroup container,int position,Object object){
        container.removeView(mlistviews.get(position % mlistviews.size()));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        //这个方法用来实例化页卡
        container.addView(mlistviews.get(position % mlistviews.size()));
        return mlistviews.get(position % mlistviews.size());
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
