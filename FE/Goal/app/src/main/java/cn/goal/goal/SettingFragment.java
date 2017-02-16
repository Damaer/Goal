package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * Created by chenlin on 16/02/2017.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ImageButton backButton;
    private RelativeLayout accountSafetyButton;
    private RelativeLayout aboutButton;
    private RelativeLayout feedbackButton;
    private Button logoutButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        backButton = (ImageButton) mView.findViewById(R.id.back);
        backButton.setOnClickListener(this);

        accountSafetyButton = (RelativeLayout) mView.findViewById(R.id.account_safe);
        accountSafetyButton.setOnClickListener(this);

        aboutButton = (RelativeLayout) mView.findViewById(R.id.about);
        aboutButton.setOnClickListener(this);

        feedbackButton = (RelativeLayout) mView.findViewById(R.id.feedback);
        feedbackButton.setOnClickListener(this);

        logoutButton = (Button) mView.findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            case R.id.account_safe:
                break;
            case R.id.about:
                break;
            case R.id.feedback:
                break;
            case R.id.logout:
                break;
        }
    }
}
