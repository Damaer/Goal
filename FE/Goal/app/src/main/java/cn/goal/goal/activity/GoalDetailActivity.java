package cn.goal.goal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import cn.goal.goal.R;
import cn.goal.goal.services.GoalUserMapService;
import cn.goal.goal.services.object.GoalFinished;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.utils.Util;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalDetailActivity extends AppCompatActivity implements View.OnClickListener, RadioButton.OnCheckedChangeListener {
    private TextView title;
    private TextView content;
    private RadioButton radioButtonFinished;
    private RadioButton radioButtonUnfinished;
    private TextView begin;
    private TextView plan;
    private TextView end;
    private TextView createAt;
    private ImageButton buttonBack;
    private ImageButton buttonMenu;
    private PopupMenu mPopupMenu;

    private String goalIndex;
    private GoalUserMap goal; // 存放goal信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);

        // 传入数据不正确
        if (getIntent() == null) {
            Toast.makeText(this, "传入数据错误", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }
        goalIndex = getIntent().getExtras().getString("goalIndex");
        goal = GoalUserMapService.getGoal(goalIndex);

        radioButtonFinished = (RadioButton) findViewById(R.id.radioButtonFinished);
        radioButtonFinished.setOnCheckedChangeListener(this);
        radioButtonUnfinished = (RadioButton) findViewById(R.id.radioButtonUnfinished);
        radioButtonUnfinished.setOnCheckedChangeListener(this);

        title = (TextView) findViewById(R.id.goal_title);
        content = (TextView) findViewById(R.id.content);end = (TextView) findViewById(R.id.end);
        begin = (TextView) findViewById(R.id.begin);
        plan = (TextView) findViewById(R.id.plan);
        createAt = (TextView) findViewById(R.id.createAt);

        buttonBack = (ImageButton) findViewById(R.id.button_back);
        buttonBack.setOnClickListener(this);
        buttonMenu = (ImageButton) findViewById(R.id.button_menu);
        buttonMenu.setOnClickListener(this);

        render();
        createMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 刷新goal数据;
        render();
    }

    private void render() {
        title.setText(goal.getGoal().getTitle());
        content.setText(goal.getGoal().getContent());
        radioButtonFinished.setChecked(goal.getFinish());
        radioButtonUnfinished.setChecked(!goal.getFinish());
        end.setText(Util.dateToString(goal.getEnd()));
        begin.setText(Util.dateToString(goal.getBegin()));
        plan.setText(Util.dateToString(goal.getPlan()));
        createAt.setText(String.format(getResources().getString(R.string.create_at), goal.getCreateAt()));
    }

    private void createMenu() {
        mPopupMenu = new PopupMenu(this, buttonMenu);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.goal_operation_popupmenu, mPopupMenu.getMenu());

        mPopupMenu.getMenu().getItem(2).setEnabled(isMarkableToday()); // 设置标记今日已完成菜单
        // 如果目标未完成则设置"标记已完成"菜单为可点击
        mPopupMenu.getMenu().getItem(3).setEnabled(!goal.getFinish());
        // 如果目标已完成则设置"标记未完成"菜单为可点击
        mPopupMenu.getMenu().getItem(4).setEnabled(goal.getFinish());

        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                final String menuEdit = getResources().getString(R.string.menu_edit);
                final String menuDelete = getResources().getString(R.string.menu_delete);
                final String menuFinishedToday = getResources().getString(R.string.menu_mark_finished_today);
                final String menuFinished = getResources().getString(R.string.menu_mark_finished);
                final String menuUnfinished = getResources().getString(R.string.menu_mark_unfinished);
                final String menuRecord = getResources().getString(R.string.menu_record);

                if (title.equals(menuEdit)) {
                    handleEdit();
                } else if (title.equals(menuDelete)) {
                    handleDelete();
                } else if (title.equals(menuFinishedToday)) {
                    handleMarkFinishedToday();
                } else if (title.equals(menuUnfinished)) {
                    handleMarkUnfinished();
                } else if (title.equals(menuFinished)) {
                    handleMarkFinished();
                } else if (title.equals(menuRecord)) {
                    handleRecord();
                }
                return true;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 设置调用setChecked方法不触发下面代码；
        if (!buttonView.isPressed()) return;

        switch (buttonView.getId()) {
            case R.id.radioButtonFinished:
                handleMarkFinished();
                break;
            case R.id.radioButtonUnfinished:
                handleMarkUnfinished();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_menu:
                mPopupMenu.show();
                break;
        }
    }

    private void handleEdit() {
        Intent intent = new Intent(this, GoalEditActivity.class);
        intent.putExtra("goalIndex", goalIndex);
        startActivity(intent);
    }

    private void handleDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_goal_dialog_info);
        builder.setTitle(R.string.delete_goal_dialog_title);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GoalUserMapService.deleteGoal(goal);
                finish();
            }
        });

        builder.create().show();
    }

    private void handleMarkFinished() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("目标已完成");
        dialog.setMessage("确认标记目标完成?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GoalUserMapService.markFinished(goal);
                radioButtonFinished.setChecked(true);
                radioButtonUnfinished.setChecked(false);
                mPopupMenu.getMenu().getItem(2).setEnabled(false); // 设置今日标记不可用
                mPopupMenu.getMenu().getItem(3).setEnabled(false); // 设置标记完成不可用
                mPopupMenu.getMenu().getItem(4).setEnabled(true); // 设置标记未完成可用
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                radioButtonFinished.setChecked(false);
                radioButtonUnfinished.setChecked(true);
            }
        });
        dialog.show();
    }

    private void handleMarkUnfinished() {
        String result = GoalUserMapService.markUnfinished(goal);
        if (result == null) {
            Toast.makeText(this, "目标标记未完成", Toast.LENGTH_SHORT).show();
            radioButtonFinished.setChecked(false);
            radioButtonUnfinished.setChecked(true);
            mPopupMenu.getMenu().getItem(2).setEnabled(isMarkableToday()); // 设置标记今日完成菜单
            mPopupMenu.getMenu().getItem(3).setEnabled(true); // 设置标记完成可用
            mPopupMenu.getMenu().getItem(4).setEnabled(false); // 设置标记未完成不可用
        } else {
            Toast.makeText(this, "标记未完成失败:" + result, Toast.LENGTH_SHORT).show();
            radioButtonFinished.setChecked(true);
            radioButtonUnfinished.setChecked(false);
        }
    }

    private void handleMarkFinishedToday() {
        String result = GoalUserMapService.markGoalFinishedToday(goal);
        if (result == null) {
            Toast.makeText(this, "目标完成！", Toast.LENGTH_SHORT).show();
            mPopupMenu.getMenu().getItem(2).setEnabled(false); // 设置今日标记不可用
        } else {
            Toast.makeText(this, "完成失败:" + result, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRecord() {
        Intent intent = new Intent(this, GoalRecordActivity.class);
        intent.putExtra("goalIndex", goalIndex);
        startActivity(intent);
    }

    /**
     * 判断当前目标今日能否被标记
     * @return
     */
    private Boolean isMarkableToday() {
        if (goal.getFinish()) return false;
        GoalFinished goalsFinished = GoalUserMapService.getGoalsFinished();
        return goalsFinished.getGoal().contains(goal.get_id());
    }
}
