package cn.goal.goal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import cn.goal.goal.utils.Share;

/**
 * Created by chenlin on 14/02/2017.
 */
public class PersonFragment extends Fragment {
    private View mView;
    private Toolbar toolbar;
    private ImageButton edit;
    private ImageButton share;
    private TableRow dailySentence;
    private TableRow statistics;
    private TableRow settings;
    private TextView description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_person, container, false);

        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        edit = (ImageButton) mView.findViewById(R.id.edit);
        share = (ImageButton) mView.findViewById(R.id.share);
        dailySentence = (TableRow) mView.findViewById(R.id.daily_sentence);
        statistics = (TableRow) mView.findViewById(R.id.statistics);
        settings = (TableRow) mView.findViewById(R.id.settings);
        description = (TextView) mView.findViewById(R.id.description);

        renderInitialData();
        addListener();
        return mView;
    }

    private void renderInitialData() {
        toolbar.setTitle("Pencil");
        description.setText(R.string.default_description);
    }

    private void addListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", "Pencil");
                bundle.putString("avatar", "pencilsky.cn");
                bundle.putString("description", description.getText().toString());
                bundle.putString("email", "9807175@qq.com");
                bundle.putString("phone", "13000000000");

                intent.putExtra("data", bundle);
                startActivityForResult(intent, EditInfoActivity.REQUEST_CODE);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(getView());
            }
        });

        dailySentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动每日一句activity
                Intent intent = new Intent(getContext(), A_Word_EverydayActivity.class);
                startActivityForResult(intent, A_Word_EverydayActivity.REQUEST_CODE);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动实时统计activity
                startActivity(new Intent(getContext(), Realtime_statisticsActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动设置页面
                startActivityForResult(new Intent(getContext(), SettingsActivity.class), SettingsActivity.LOGOUT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditInfoActivity.REQUEST_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras().getBundle("data");
                System.out.println(bundle.getString("username"));
                System.out.println(bundle.getString("avatar"));
                System.out.println(bundle.getString("description"));
                System.out.println(bundle.getString("email"));
                System.out.println(bundle.getString("phone"));
            }
        } else if (resultCode == SettingsActivity.LOGOUT) {
            // 退出账号
            getActivity().finish();
        }
    }

    public void shareText(View view) {
        Share.shareText(getContext(), "分享内容");
    }
}
