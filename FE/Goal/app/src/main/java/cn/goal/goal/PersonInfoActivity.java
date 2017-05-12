package cn.goal.goal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.goal.goal.util.DisplayUtil;
import cn.goal.goal.util.FastBlurUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlin on 04/05/2017.
 */
public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.headUserInfo)
    RelativeLayout headUserInfo;

    /**
     * 显示用户名的TextView
     */
    @Bind(R.id.usernameTextView)
    TextView usernameTexiView;
    /**
     * 显示用户简介的TextView
     */
    @Bind(R.id.descriptionTextView)
    TextView descriptionTextView;
    /**
     * 显示用户头像的ImageView
     */
    @Bind(R.id.avatarImageView)
    ImageView avatarImageView;
    /**
     * 关注/取消关注/编辑资料按钮
     */
    @Bind(R.id.followOrEditTextView)
    TextView followOrEditTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);

        init();

        DisplayUtil.setTranslucentStatus(this);
    }

    private void init() {
        // tool bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(this);

        // collapsingToolbarLayout
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setTitle("Pencil");
            // 将头像高斯模糊后设置为背景
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.word);
        bitmap = FastBlurUtil.toBlur(bitmap, 10);
        Drawable blurDrawable = new BitmapDrawable(bitmap);
        collapsingToolbarLayout.setBackground(blurDrawable);

        // 设置ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(BaseFragment.newInstance("他的目标"));
        viewPagerAdapter.addFragment(BaseFragment.newInstance("他的记录"));
        viewPagerAdapter.addFragment(BaseFragment.newInstance("他的动态"));
        viewPager.setAdapter(viewPagerAdapter);
        // 将TabLayout与ViewPager绑定
        tabLayout.setupWithViewPager(viewPager);
        // 设置tabItem 在执行setupWithViewPager后执行。 setupWithViewPager方法将tablayout中所有tab移除并重新创建
        tabLayout.getTabAt(0).setText("他的目标");
        tabLayout.getTabAt(1).setText("他的记录");
        tabLayout.getTabAt(2).setText("他的动态");
    }

    /**
     * 菜单返回键
     * @param v
     */
    @Override
    public void onClick(View v) {
        finish();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
