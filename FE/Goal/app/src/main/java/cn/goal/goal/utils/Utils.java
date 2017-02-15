package cn.goal.goal.utils;

import android.graphics.*;

/**
 * Created by chenlin on 14/02/2017.
 */
public class Utils {
    /**
     * 图片圆角化
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int lenMin = Math.min(bitmap.getWidth(), bitmap.getHeight());
        float radius = lenMin / 2; // 圆角半径

        Bitmap output = Bitmap.createBitmap(lenMin, lenMin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Rect dstRect = new Rect(0, 0, lenMin, lenMin);
        final Rect srcRect = new Rect((bitmap.getWidth() - lenMin) / 2, (bitmap.getHeight() - lenMin) / 2, lenMin, lenMin);

        final Paint paint = new Paint();
        paint.setAntiAlias(true); // 抗锯齿
        canvas.drawARGB(0, 0, 0, 0); // 设置填充色为透明
        canvas.drawRoundRect(new RectF(dstRect), radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); // 设置填充交集部分
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
        return output;
    }
}