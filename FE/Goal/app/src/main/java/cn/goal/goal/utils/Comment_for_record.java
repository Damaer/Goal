package cn.goal.goal.utils;

import java.util.Calendar;

/**
 * Created by 97617 on 2017/2/25.
 */

public class Comment_for_record {
    private int user_photo;
    private String user_name;
    private String replyed_name;
    private String comment_content;
    private String comment_time;
    public Comment_for_record(int user_photo,String user_name,String replyed_name,String comment_content,String comment_time){
        this.user_photo=user_photo;
        this.user_name=user_name;
        this.replyed_name=replyed_name;
        this.comment_content=comment_content;
        this.comment_time=comment_time;
    }
    public int getuser_photo()
    {
        return user_photo;
    }
    public String getuser_name()
    {
        return user_name;
    }
    public String getreplyed_name()
    {
        return replyed_name;
    }
    public String getcomment_content()
    {
        return comment_content;
    }
    public String getcomment_time()
    {
        return comment_time;
    }

}
