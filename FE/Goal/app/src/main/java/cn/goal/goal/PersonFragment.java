package cn.goal.goal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.goal.goal.services.UserService;
import cn.goal.goal.services.object.GetBitmapInterface;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.Share;

/**
 * Created by chenlin on 14/02/2017.
 */
public class PersonFragment extends Fragment {
    private View mView;
    private ImageView avatarView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageButton edit;
    private ImageButton share;
    private TableRow dailySentence;
    private TableRow statistics;
    private TableRow settings;
    private TextView description;
    private TableRow contact;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_person, container, false);

        avatarView = (ImageView) mView.findViewById(R.id.avatar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.collapsingToolbarLayout);
        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        edit = (ImageButton) mView.findViewById(R.id.edit);
        share = (ImageButton) mView.findViewById(R.id.share);
        dailySentence = (TableRow) mView.findViewById(R.id.daily_sentence);
        statistics = (TableRow) mView.findViewById(R.id.statistics);
        settings = (TableRow) mView.findViewById(R.id.settings);
        description = (TextView) mView.findViewById(R.id.description);
        contact= (TableRow) mView.findViewById(R.id.contact_us);
        user = UserService.getUserInfo();
        renderInitialData();
        addListener();
        return mView;
    }

    private void renderInitialData() {
        // 获取头像
        user.setAvatarInterface(new GetBitmapInterface() {
            @Override
            public void getImg(Bitmap avatar) {
                avatarView.setImageBitmap(avatar);
            }

            @Override
            public void error(String errorInfo) {
                Toast.makeText(getContext(), errorInfo, Toast.LENGTH_SHORT).show();
            }
        });
        user.getAvatarBitmap(getContext());

        // 通过设置CollapsingToolbarLayout改变toolbar标题
        mCollapsingToolbarLayout.setTitle(user.getUsername());
//        toolbar.setTitle(user.getUsername()); 此方法改变标题无效
        description.setText(user.getDescription());
    }

    private void addListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
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
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditInfoActivity.REQUEST_CODE) {
            // 刷新数据
            renderInitialData();
        } else if (resultCode == SettingsActivity.LOGOUT) {
            // 退出账号
            getActivity().finish();
        }
    }

    public void shareText(View view) {
        Share.shareText(getContext(), "分享内容");
    }
}
