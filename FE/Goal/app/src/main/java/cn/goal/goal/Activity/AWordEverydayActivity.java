package cn.goal.goal.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.goal.goal.Dialog.LoadingDialog;
import cn.goal.goal.R;
import cn.goal.goal.Services.DailySentenceService;
import cn.goal.goal.Services.object.DailySentence;
import cn.goal.goal.Services.object.GetBitmapInterface;
import cn.goal.goal.Utils.RoundCorner;
import cn.goal.goal.Utils.Share;

public class AWordEverydayActivity extends AppCompatActivity implements GetBitmapInterface {
    public static final int REQUEST_CODE = 5;

    private ImageView background;
    private ImageButton comeback;
    private ImageButton share;
    private TextView sentenceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__word__everyday);

        background = (ImageView) findViewById(R.id.background);
        comeback= (ImageButton) findViewById(R.id.comeback);
        share=(ImageButton) findViewById(R.id.share);
        sentenceView = (TextView) findViewById(R.id.sentenceView);

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

        new FetchDataTask().execute();
    }

    private void render(DailySentence dailySentence) {
        if (dailySentence == null) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        dailySentence.setOnGetImgLisener(this);
        dailySentence.getAvatarBitmap(this);
        sentenceView.setText(dailySentence.getSentence());
    }

    @Override
    public void getImg(Bitmap img) {
        background.setImageBitmap(img);
    }

    @Override
    public void error(String errorInfo) {
        System.out.println(errorInfo);
        Toast.makeText(this, "获取每日一句图片失败", Toast.LENGTH_SHORT).show();
    }

    class FetchDataTask extends AsyncTask<Void, Void, DailySentence> {
        LoadingDialog mLoadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadingDialog().showLoading(AWordEverydayActivity.this);
        }

        @Override
        protected DailySentence doInBackground(Void... params) {
            return DailySentenceService.getDailySentenceToday();
        }

        @Override
        protected void onPostExecute(DailySentence dailySentence) {
            super.onPostExecute(dailySentence);
            cancelDialog();
            render(dailySentence);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancelDialog();
        }

        private void cancelDialog() {
            if (mLoadingDialog != null)
                mLoadingDialog.closeDialog();
        }
    }
}