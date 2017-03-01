package cn.goal.goal;

import android.graphics.Bitmap;
import cn.goal.goal.services.object.GetBitmapInterface;

/**
 * Created by chenlin on 27/02/2017.
 */
public class GetBitmapListener implements GetBitmapInterface {
    public int tag;
    public GetBitmapListener(int tag) {
        this.tag = tag;
    }

    @Override
    public void getImg(Bitmap img) {

    }

    @Override
    public void error(String errorInfo) {

    }
}
