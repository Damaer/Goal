package cn.goal.goal.Services.object;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import cn.goal.goal.Services.Config;
import cn.goal.goal.Utils.HttpRequest;
import cn.goal.goal.Utils.RoundCorner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created by chenlin on 20/02/2017.
 */
public class DailySentence {
    private String sentence;
    private String backImg;
    private Date date;

    private Bitmap backImgBitmap;
    private Boolean isFetchingBackImg = false;

    private GetBitmapInterface mGetBitmapInterface;

    public DailySentence(String sentence, String backImg, Date date) {
        this.sentence = sentence;
        this.backImg = backImg;
        this.date = date;
    }

    public String getSentence() {
        return sentence;
    }

    public String getBackImg() {
        return backImg;
    }

    public Date getDate() {
        return date;
    }

    public void getAvatarBitmap(Context context) {
        if (backImgBitmap != null) {
            if (mGetBitmapInterface != null)
                mGetBitmapInterface.getImg(backImgBitmap);
            return ;
        }
        if (isFetchingBackImg) return ;
        new FetchImgTask(context).execute();
    }

    public void setOnGetImgLisener(GetBitmapInterface avatarInterface) {
        this.mGetBitmapInterface = avatarInterface;
    }

    public class FetchImgTask extends AsyncTask<Void, Void, String> {
        private Context mContext;

        public FetchImgTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFetchingBackImg = true;
        }

        @Override
        protected String doInBackground(Void... params) {
            File file = new File(mContext.getExternalCacheDir(), getFileName(backImg));

            // 判断头像是否已下载到本地
            if (!file.exists()) {
                // 从服务器下载头像到本地
                try {
                    HttpRequest request = HttpRequest.get(Config.apiServer + backImg);
                    if (request.ok()) {
                        request.receive(file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "下载头像失败";
                }
            }

            try {
                // 将头像文件转换为bitmap
                Uri uri = Uri.fromFile(file);
                ContentResolver cr = mContext.getContentResolver();
                backImgBitmap = RoundCorner.toRoundCorner(BitmapFactory.decodeStream(cr.openInputStream(uri)), 20);
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "下载头像失败";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isFetchingBackImg = false;
            if (mGetBitmapInterface != null) {
                if (s == null) {
                    mGetBitmapInterface.getImg(backImgBitmap);
                } else {
                    mGetBitmapInterface.error(s);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isFetchingBackImg = false;
        }

        /**
         * 获取服务器上的文件名
         * 如'/public/avatar/asdasdasd',则返回'asdasdasd'
         * @param avatar
         * @return
         */
        private String getFileName(String avatar) {
            return avatar.substring(avatar.lastIndexOf('/') + 1);
        }
    }
}
