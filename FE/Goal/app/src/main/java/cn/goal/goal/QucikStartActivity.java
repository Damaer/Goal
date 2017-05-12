package cn.goal.goal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.List;

import cn.goal.goal.util.DisplayUtil;

import static cn.goal.goal.R.*;

public class QucikStartActivity extends AppCompatActivity implements View.OnClickListener {
    public int progress;
    //专注时间
    public AttentionTimeClass attentiontime;
    //计时器
    public CountDownTimer timer;
    // 单选提示框
    private AlertDialog alertDialog2;
    public ViewPager viewPager;
    public DonutProgress donutProgress;
    public ImageButton comeback;
    public ImageButton setting;
    public Button button;
    public QucikStartViewPagerAdapter qucikStartViewPagerAdapter;
    private List<View> viewPages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_qucik_start);
        //全屏显示
        DisplayUtil.setTranslucentStatus(this);
        initPageAdapter();
        initView();
    }
    private void initView() {
        progress=0;
        donutProgress= (DonutProgress) findViewById(id.donut_progress);
        comeback= (ImageButton) findViewById(id.comeback);
        comeback.setOnClickListener(this);
        setting= (ImageButton) findViewById(id.setting);
        setting.setOnClickListener(this);
        button= (Button) findViewById(id.button);
        button.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(id.viewPager);
        viewPager.setAdapter(qucikStartViewPagerAdapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initPageAdapter() {
        /**
         * 对于这几个想要动态载入的page页面，使用LayoutInflater.inflate()来找到其布局文件，并实例化为View对象
         */
        LayoutInflater inflater = LayoutInflater.from(this);
        View page1 = inflater.inflate(layout.quickstart_page1, null);
        View page2 = inflater.inflate(layout.quickstart_page2, null);
        View page3 = inflater.inflate(layout.quickstart_page3, null);
        View page4 = inflater.inflate(layout.quickstart_page4, null);
        //添加到集合中
        viewPages.add(page1);
        viewPages.add(page2);
        viewPages.add(page3);
        viewPages.add(page4);
        qucikStartViewPagerAdapter=new QucikStartViewPagerAdapter(viewPages);
    }
    public void initpopwindow(){
        TopRightMenu mTopRightMenu = new TopRightMenu(QucikStartActivity.this);

//添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        mTopRightMenu
                .setHeight(300)     //默认高度480
                .setWidth(340)      //默认宽度wrap_content
                .showIcon(false)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .addMenuItem(new MenuItem("设置专注时长"))
                .addMenuItem(new MenuItem("每日目标时长"))
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position)
                        {
                            case 0:
                                settingattiontime();
                                break;
                            case 1:
                                settinggoalattiontime();
                                break;
                            default:
                                break;
                        }

                    }
                })
                .showAsDropDown(setting, -225, 0);    //带偏移量
    }
    public void settingattiontime(){
        final String[] items = {"5分钟","10分钟","15分钟","25分钟","30分钟","45分钟","60分钟","75分钟","90分钟","105分钟","120分钟"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("设置专注时长");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int index) {
                    attentiontime=new AttentionTimeClass(items[index]);
                    donutProgress.setText(attentiontime.gettime());
                    donutProgress.setProgress(0);
                    button.setText("开始");
                    timer.cancel();
                    progress=0;
                    alertDialog2.dismiss();
                }
            });
            alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO 业务逻辑代码

                    // 关闭提示框
                    alertDialog2.dismiss();
                }
            });
            alertDialog2 = alertBuilder.create();
            alertDialog2.show();
        }
    public void settinggoalattiontime(){
        final String[] items = {"5分钟","10分钟","15分钟","25分钟","30分钟","45分钟","60分钟","75分钟","90分钟","105分钟","120分钟"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("今日目标专注时长");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                alertDialog2.dismiss();
            }
        });
        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO 业务逻辑代码

                // 关闭提示框
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case id.comeback:
                finish();
                break;
            case id.setting:
                initpopwindow();
                break;
            case id.button:
            {
                if (button.getText().equals("开始"))
                {
                    StringBuffer progresstext=new StringBuffer(donutProgress.getText());
                    String time=progresstext.substring(0,progresstext.indexOf(":"))+"分钟";
                    attentiontime=new AttentionTimeClass(time);
                    donutProgress.setText(attentiontime.gettime());
                    donutProgress.setMax(attentiontime.minute*60+attentiontime.second);
                    donutProgress.setProgress(0);
                    button.setText("暂停");
                    timer = new CountDownTimer(attentiontime.minute*1000*60+attentiontime.second*1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(attentiontime.reduce()==0) {
                                button.setText("完成");
                                this.cancel();
                            }
                            else {
                                donutProgress.setText(attentiontime.gettime());
                                progress++;
                                donutProgress.setProgress(progress);
                            }
                        }
                        @Override
                        public void onFinish() {
                            donutProgress.setText(attentiontime.gettime());
                            button.setText("完成");
                        }
                    };
                    timer.start();
                }
                else if(button.getText().equals("暂停"))
                {
                    button.setText("继续");
                    timer.cancel();
                }
                else if(button.getText().equals("继续"))
                {
                    timer.start();
                    button.setText("暂停");
                }
                else if(button.getText().equals("完成"))
                {

                }
            }
                break;
            default:
                break;
        }
    }
}
