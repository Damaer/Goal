package cn.goal.goal.services.object;

import android.graphics.Bitmap;

/**
 * Created by chenlin on 26/02/2017.
 */
public interface GetAvatarBitmapInterface {
    void getAvatar(Bitmap avatar);
    void error(String errorInfo);
}
