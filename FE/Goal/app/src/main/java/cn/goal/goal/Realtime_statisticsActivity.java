package cn.goal.goal;


import java.util.ArrayList;
        import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
        import android.animation.ObjectAnimator;
        import android.graphics.Color;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.view.ViewPager;
        import android.support.v4.view.ViewPager.OnPageChangeListener;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
        import android.widget.TextView;
public class Realtime_statisticsActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener
{
    private ImageButton comeback;
    private ViewPager myViewPager;
    // 要使用的ViewPager
    private View page1, page2, page3;
    // ViewPager包含的页面
    private List<View> pageList;
    // ViewPager包含的页面列表，一般给adapter传的是一个list
    // 适配器
    private TextView tv_tab0, tv_tab1, tv_tab2;
    // 3个选项卡
    private ImageView line_tab;
    // tab选项卡的下划线
    private int moveOne = 0;
    // 下划线移动一个选项卡
    private boolean isScrolling = false;
    // 手指是否在滑动
    private boolean isBackScrolling = false;
    // 手指离开后的回弹
    private long startTime = 0;
    private long currentTime = 0;
    @Override protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_statistics);
        comeback=(ImageButton) findViewById(R.id.come_back_to_mine);
        comeback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        initLineImage();
    }
    /** * 重新设定line的宽度 */
    private void initLineImage()
    {
        // TODO Auto-generated method stub
        /** * 获取屏幕的宽度 */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        moveOne =dip2px(this,80);
    }

    /**
     *下面的函数把dp转换成为px，为了计算下划线移动的长度
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void initView() {
        // TODO Auto-generated method stub

        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        Realtime_statistics_stars_Fragment myFragment1 = new Realtime_statistics_stars_Fragment();
        Realtime_statistics_count_fragment myFragment2 = new Realtime_statistics_count_fragment();
        Realtime_statistics_ranking_Fragment myFragment3 = new Realtime_statistics_ranking_Fragment ();
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        fragmentList.add(myFragment3);
        Realtime_FragmentAdapter myFragmentAdapter = new Realtime_FragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
        tv_tab0 = (TextView) findViewById(R.id.tv_tab0);
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        myViewPager.setCurrentItem(0);
        tv_tab0.setTextColor(Color.rgb(0,0,0));
        tv_tab1.setTextColor(Color.rgb(105,105,105));
        tv_tab2.setTextColor(Color.rgb(105,105,105));
        tv_tab0.setOnClickListener(this);
        tv_tab1.setOnClickListener(this);
        tv_tab2.setOnClickListener(this);
        myViewPager.setOnPageChangeListener(this);
        line_tab = (ImageView) findViewById(R.id.line_tab1);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
        switch (state)
        {
            case 1: isScrolling = true;
                isBackScrolling = false;
                break;
            case 2:
                isScrolling = false;
                isBackScrolling = true;
                break;
            default:
                isScrolling = false;
                isBackScrolling = false;
                break;
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        // TODO Auto-generated method stub
        currentTime = System.currentTimeMillis();
        if (isScrolling && (currentTime - startTime > 200))
        { movePositionX(position, moveOne * positionOffset);
            startTime = currentTime;
        }
        if (isBackScrolling)
        {
            movePositionX(position);
        }
    }

    @Override public void onPageSelected(int position)
    { // TODO Auto-generated method stub
        switch (position)
        { case 0:
            tv_tab0.setTextColor(Color.BLACK);
            tv_tab1.setTextColor(Color.rgb(105,105,105));
            tv_tab2.setTextColor(Color.rgb(105,105,105));
            movePositionX(0);
            break;
            case 1:
                tv_tab0.setTextColor(Color.rgb(105,105,105));
                tv_tab1.setTextColor(Color.BLACK);
                tv_tab2.setTextColor(Color.rgb(105,105,105));
                movePositionX(1);
                break;
            case 2:
                tv_tab0.setTextColor(Color.rgb(105,105,105));
                tv_tab1.setTextColor(Color.rgb(105,105,105));
                tv_tab2.setTextColor(Color.BLACK);
                movePositionX(2);
                break;
            default:
                break;
        }
    }
    /** * 下划线跟随手指的滑动而移动 * @param toPosition * @param positionOffsetPixels */
    private void movePositionX(int toPosition, float positionOffsetPixels)
    { // TODO Auto-generated method stub
        float curTranslationX = line_tab.getTranslationX();
        float toPositionX = moveOne * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(line_tab, "translationX", curTranslationX, toPositionX);
        animator.setDuration(500);
        animator.start();
    } /** * 下划线滑动到新的选项卡中 * @param toPosition */
private void movePositionX(int toPosition)
{ // TODO Auto-generated method stub
    movePositionX(toPosition, 0);
}
    @Override
public void onClick(View v)
{
// TODO Auto-generated method stub
    switch (v.getId())
    { case R.id.tv_tab0:
        myViewPager.setCurrentItem(0);
        break;
        case R.id.tv_tab1:
            myViewPager.setCurrentItem(1);
            break;
        case R.id.tv_tab2:
            myViewPager.setCurrentItem(2);
            break;
        default: break;
    }
}
}
