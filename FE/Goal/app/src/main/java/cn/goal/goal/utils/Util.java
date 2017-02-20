package cn.goal.goal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
