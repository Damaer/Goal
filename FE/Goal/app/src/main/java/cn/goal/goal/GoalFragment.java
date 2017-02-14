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

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalFragment extends Fragment {
    private View mView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    private TabLayout.Tab finishedTab;
    private TabLayout.Tab unfinishedTab;

    private FloatingActionButton addGoal;

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
            addGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoalAddActivity.class);
                    startActivityForResult(intent, GoalAddActivity.REQUEST_CODE);
                }
            });
        }

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoalAddActivity.REQUEST_CODE) {
            if (data != null) {
                Bundle goalInfo = data.getExtras().getBundle("data");
                System.out.println(goalInfo.getString("title"));
                System.out.println(goalInfo.getString("content"));
                System.out.println(goalInfo.getString("begin"));
                System.out.println(goalInfo.getString("plan"));
            }
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = {"已完成", "未完成"};
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
            return position == 0 ? finishedGoal : unfinishedGoal;
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
