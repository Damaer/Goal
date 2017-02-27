package cn.goal.goal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import cn.goal.goal.services.FocusTimeService;
import cn.goal.goal.utils.NetWorkUtils;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import cn.goal.goal.services.UserService;

import java.util.Date;
import java.util.HashSet;

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

        /*
            从服务器上获取用户信息，并判断登录信息是否过期，过期则要求用户重新登录
         */
        if (NetWorkUtils.isNetworkConnected(this)) {
            new FetchUserInfoTask().execute();
        }
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

    class FetchUserInfoTask extends AsyncTask<Void, Void, Boolean> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(BaseActivity.this);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return UserService.fetchUserInfo();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            cancelDialog();
            if (aBoolean) {
                // 登录信息过期，启动登录界面
                UserService.removeLocalInfo();
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                finish();
            } else {
                // 将专注时间上传到服务器上
                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                HashSet<String> focusTime = (HashSet<String>) sp.getStringSet("focus_time", null);
                if (focusTime != null) {
                    for (String row : focusTime) {
                        String[] split = row.split("-");
                        if (split.length == 2) {
                            new UploadFocusTimeTask(new Date(Long.valueOf(split[0])), Integer.valueOf(split[1]));
                        }
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) mLoadingDialog.closeDialog();
        }
    }

    class UploadFocusTimeTask extends AsyncTask<Void, Void, String> {
        LoadingDialog mLoadingDialog;
        private Date begin;
        private int length;

        public UploadFocusTimeTask(Date begin, int length) {
            super();
            this.begin = begin;
            this.length = length;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(BaseActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            return FocusTimeService.addFocusTime(begin, length);
        }

        @Override
        protected void onPostExecute(String err) {
            super.onPostExecute(err);
            cancelDialog();
            if (err == null) {
                SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                HashSet<String> focusTime = (HashSet<String>) sp.getStringSet("focus_time", new HashSet<String>());
                focusTime.remove(String.valueOf(begin.getTime()) + "-" + String.valueOf(length));
                sp.edit().putStringSet("focus_time", focusTime).apply();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null) {
                mLoadingDialog.closeDialog();
            }
        }
    }

}
