package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import cn.goal.goal.services.UserService;

/**
 * Created by chenlin on 12/02/2017.
 */
public class BaseActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar mBottomNavigationBar; // 导航栏组件
    private ViewGroup container; // activity_base layout
    private LinearLayout content; // 用于存放子类视图
    private int pos = 0;

    private Fragment[] mFragments = new Fragment[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserService.initData(getSharedPreferences("user", MODE_PRIVATE), this);
        //未登录则启动登录界面
        if (UserService.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        mFragments[0] = new GoalFragment();
        mFragments[1] = new NoteListFragment();
        mFragments[2] = new RecommendFragment();
        mFragments[3] = new PersonFragment();

        initNavigationBar();
    }

    private void initNavigationBar() {
        container = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mBottomNavigationBar = (BottomNavigationBar) container.findViewById(R.id.bottom_bar);
        content = (LinearLayout) container.findViewById(R.id.content_view);

        initNavigationItems();

        super.setContentView(container);
        setFragment(0); // setDefaultFragment
    }

    private void initNavigationItems() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_adjust_black_24dp,"目标"))
                .addItem(new BottomNavigationItem(R.drawable.ic_note_black_24dp,"便签"))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_black_24dp,"推荐"))
                .addItem(new BottomNavigationItem(R.drawable.ic_person_black_24dp,"设置"))
                .setActiveColor(R.color.colorAccent)
                .setFirstSelectedPosition(pos)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void setContentView(View view) {
        content.addView(view);
    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, content, true);
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        content.addView(view);
    }

    private void setFragment(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_view, mFragments[pos]);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        setFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserService.getDB().close();
    }
}
