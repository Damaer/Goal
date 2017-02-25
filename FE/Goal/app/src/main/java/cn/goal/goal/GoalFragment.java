package cn.goal.goal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalFragment extends Fragment implements View.OnClickListener {
    private View mView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    private TabLayout.Tab finishedTab;
    private TabLayout.Tab unfinishedTab;

    private FloatingActionButton addGoal;
    private Button quickStart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_goal, container, false);

            mViewPager = (ViewPager) mView.findViewById(R.id.goal_viewpager);
            mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
            mViewPager.setAdapter(mMyFragmentPagerAdapter);

            mTabLayout = (TabLayout) mView.findViewById(R.id.goal_tablayout);
            mTabLayout.setupWithViewPager(mViewPager);

            finishedTab = mTabLayout.getTabAt(0);
            unfinishedTab = mTabLayout.getTabAt(1);

            addGoal = (FloatingActionButton) mView.findViewById(R.id.add_goal);
            addGoal.setOnClickListener(this);

            quickStart = (Button) mView.findViewById(R.id.quick_start);
            quickStart.setOnClickListener(this);
        }

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goal:
                startActivity(new Intent(getActivity(), GoalAddActivity.class));
                break;
            case R.id.quick_start:
                handleQuickStart();
                break;
        }
    }

    private void handleQuickStart() {
        TimePicker picker = new TimePicker(getActivity(), TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);
        picker.setRangeEnd(23, 0);
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
//                showToast(hour + ":" + minute);
                Intent intent = new Intent(getContext(), QuickStartCountTime.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                startActivity(intent);
            }
        });
        picker.show();
        picker.setSelectedItem(0, 0);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = {"未完成", "已完成"};
        private GoalListFragment finishedGoal;
        private GoalListFragment unfinishedGoal;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);

            Bundle args = new Bundle();
            args.putBoolean("data", true);
            finishedGoal = new GoalListFragment();
            finishedGoal.setArguments(args);

            args = new Bundle();
            args.putBoolean("data", false);
            unfinishedGoal = new GoalListFragment();
            unfinishedGoal.setArguments(args);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? unfinishedGoal : finishedGoal;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
