package cn.goal.goal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenlin on 19/02/2017.
 */
public class Util {
    /*
   将date数据格式化为"yyyy-mm-dd"
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 判断字符串email是否为邮箱
     */
    public static boolean checkEmailFormate(String email) {
        String reg = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(email);
        return m.matches();
    }

    /**
     * 判断字符串phone是否为手机号
     */
    public static boolean checkPhoneFormate(String phone) {
        String reg = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(phone);
        return m.matches();
    }
}
