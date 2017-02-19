package cn.goal.goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by chenlin on 18/02/2017.
 */
public class AboutActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton closeButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        closeButton = (ImageButton) findViewById(R.id.close);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
