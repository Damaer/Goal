package cn.goal.goal;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import cn.goal.goal.utils.RoundCorner;
import cn.goal.goal.utils.Share;

public class A_Word_EverydayActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 5;

    private ImageView background;
    private ImageButton comeback;
    private ImageButton share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__word__everyday);

        background = (ImageView) findViewById(R.id.background);
        comeback= (ImageButton) findViewById(R.id.comeback);
        share=(ImageButton) findViewById(R.id.share);

        background.setImageBitmap(
                RoundCorner.toRoundCorner(BitmapFactory.decodeResource(getResources(), R.drawable.word), 20));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share.shareText(view.getContext(), "要分享的文字");
            }
        });

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
