package cn.goal.goal.services.object;

import android.graphics.Bitmap;

/**
 * Created by chenlin on 26/02/2017.
 */
public interface GetBitmapInterface {
    void getImg(Bitmap img);
    void error(String errorInfo);
}
