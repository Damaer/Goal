package cn.goal.goal.services.object;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import cn.goal.goal.services.Config;
import cn.goal.goal.utils.HttpRequest;
import cn.goal.goal.utils.RoundCorner;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by chenlin on 19/02/2017.
 */
public class User {
    private String username;
    private String avatar;
    private String description;
    private String email;
    private String phone;
    private String _id;
    private transient Bitmap avatarBitmap;
    private transient Bitmap roundAvatarBitmap;
    private transient GetBitmapInterface avatarInterface;

    private boolean isRound;
    private boolean isFetchingAvatar = false; // 标记是否正在获取头像

    public User(String _id, String username, String avatar, String description, String email, String phone) {
        this._id = _id;
        this.username = username;
        this.avatar = avatar;
        this.description = description;
        this.email = email;
        this.phone = phone;
    }

    /**
     * 异步获取用户头像图片
     * 实现接口GetAvatarBitmapInterface以获取头像
     */
    public void getAvatarBitmap(Context context) {
        getAvatarBitmap(context, true);
    }

    /**
     * 异步获取用户头像图片
     * 实现接口GetAvatarBitmapInterface以获取头像
     * @param isRound 是否圆角化头像
     */
    public void getAvatarBitmap(Context context, Boolean isRound) {
        this.isRound = isRound;
        if (avatarBitmap != null) {
            if (avatarInterface != null)
                avatarInterface.getImg(isRound ? roundAvatarBitmap : avatarBitmap);
            return ;
        }
        if (isFetchingAvatar) return ;
        new FetchAvatarTask(context).execute();
    }

    public void setAvatarInterface(GetBitmapInterface avatarInterface) {
        this.avatarInterface = avatarInterface;
    }

    public String get_id() {
        return _id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email.equals("null") ? "未绑定" : email;
    }

    public String getPhone() {
        return phone.equals("null") ? "未绑定" : phone;
    }

    public class FetchAvatarTask extends AsyncTask<Void, Void, String> {
        private Context mContext;

        public FetchAvatarTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFetchingAvatar = true;
        }

        @Override
        protected String doInBackground(Void... params) {
            File file = new File(mContext.getExternalCacheDir(), getAvatarFileName(avatar));

            // 判断头像是否已下载到本地
            if (!file.exists()) {
                // 从服务器下载头像到本地
                try {
                    HttpRequest request = HttpRequest.get(Config.apiServer + avatar);
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
                avatarBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                roundAvatarBitmap = RoundCorner.toCircle(avatarBitmap);
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "下载头像失败";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isFetchingAvatar = false;
            if (avatarInterface != null) {
                if (s == null) {
                    avatarInterface.getImg(isRound ? roundAvatarBitmap : avatarBitmap);
                } else {
                    avatarInterface.error(s);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isFetchingAvatar = false;
        }

        /**
         * 获取服务器上的头像文件名
         * 如'/public/avatar/asdasdasd',则返回'asdasdasd'
         * @param avatar
         * @return
         */
        private String getAvatarFileName(String avatar) {
            return avatar.substring(avatar.lastIndexOf('/') + 1);
        }
    }
}
