package cn.goal.goal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Created by chenlin on 13/02/2017.
 */
public class GoalDetailActivity extends AppCompatActivity {
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

    private Bundle data; // 存放goal信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);

        // 传入数据不正确
        if (getIntent() == null || getIntent().getExtras().getBundle("data") == null) {
            Toast.makeText(this, "传入数据错误", 1000).show();
            finish();
            return ;
        }

        title = (TextView) findViewById(R.id.goal_title);
        content = (TextView) findViewById(R.id.content);
        radioButtonFinished = (RadioButton) findViewById(R.id.radioButtonFinished);
        radioButtonUnfinished = (RadioButton) findViewById(R.id.radioButtonUnfinished);
        end = (TextView) findViewById(R.id.end);
        begin = (TextView) findViewById(R.id.begin);
        plan = (TextView) findViewById(R.id.plan);
        createAt = (TextView) findViewById(R.id.createAt);
        buttonBack = (ImageButton) findViewById(R.id.button_back);
        buttonMenu = (ImageButton) findViewById(R.id.button_menu);

        data = getIntent().getExtras().getBundle("data");
        render();
        createMenu();
        addListener();
    }

    private void render() {
        title.setText(data.getString("title"));
        content.setText(data.getString("content"));
        radioButtonFinished.setChecked(data.getBoolean("finished"));
        radioButtonUnfinished.setChecked(!data.getBoolean("finished"));
        end.setText(data.getString("end"));
        begin.setText(data.getString("begin"));
        plan.setText(data.getString("plan"));
        createAt.setText(String.format(getResources().getString(R.string.create_at), data.getString("createAt")));
    }

    private void createMenu() {
        mPopupMenu = new PopupMenu(this, buttonMenu);
        mPopupMenu.getMenuInflater()
                .inflate(R.menu.goal_operation_popupmenu, mPopupMenu.getMenu());

        mPopupMenu.getMenu().getItem(3).setEnabled(!data.getBoolean("finished"));
        mPopupMenu.getMenu().getItem(4).setEnabled(data.getBoolean("finished"));

        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                final String menuEdit = getResources().getString(R.string.menu_edit);
                final String menuDelete = getResources().getString(R.string.menu_delete);
                final String menuFinishedToday = getResources().getString(R.string.menu_mark_finished_today);
                final String menuFinished = getResources().getString(R.string.menu_mark_finished);
                final String menuUnfinished = getResources().getString(R.string.menu_mark_unfinished);

                if (title.equals(menuEdit)) {
                    // 编辑
                    handleEdit();
                } else if (title.equals(menuDelete)) {
                    // 删除
                    handleDelete();
                } else if (title.equals(menuFinishedToday)) {
                    // 标记今日完成
                    handleMarkFinishedToday();
                } else if (title.equals(menuUnfinished)) {
                    // 标记目标未完成
                    handleMarkUnfinished();
                } else if (title.equals(menuFinished)) {
                    // 标记目标完成
                    handleMarkFinished();
                }
                return true;
            }
        });
    }

    private void addListener() {
        radioButtonFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("标记目标完成");
                }
            }
        });

        radioButtonUnfinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("标记目标未完成");
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });
    }

    private void handleEdit() {
        Intent intent = new Intent(this, GoalEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title.getText().toString());
        bundle.putString("content", content.getText().toString());
        bundle.putString("begin", begin.getText().toString());
        bundle.putString("plan", plan.getText().toString());
        bundle.putBoolean("finished", radioButtonFinished.isChecked());

        intent.putExtra("data", bundle);
        startActivityForResult(intent, GoalEditActivity.REQUEST_CODE);
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
                System.out.println("确认删除" + data.getString("title"));
                finish();
            }
        });

        builder.create().show();
    }

    private void handleMarkFinished() {

    }

    private void handleMarkUnfinished() {

    }

    private void handleMarkFinishedToday() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoalEditActivity.REQUEST_CODE) {
            if (data != null) {
                Bundle goalInfo = data.getExtras().getBundle("data");
                System.out.println(goalInfo.getString("title"));
                System.out.println(goalInfo.getString("content"));
                System.out.println(goalInfo.getString("begin"));
                System.out.println(goalInfo.getString("plan"));
                System.out.println(goalInfo.getBoolean("finished") ? "finished" : "unfinsihed");
            }
        }
    }
}
