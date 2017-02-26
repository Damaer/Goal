package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/26.
 */

public class ContactClass {
    private int head_photo_for_contact;
    private String username_for_contact;
    private String content_of_contact;
    private String time_of_contact;
    public ContactClass(int head_photo_for_contact,String username_for_contact,String content_of_contact,String time_of_contact)
    {
        this.head_photo_for_contact=head_photo_for_contact;
        this.username_for_contact=username_for_contact;
        this.content_of_contact=content_of_contact;
        this.time_of_contact=time_of_contact;

    }
    public int  getheadphoto_for_contact()
    {
        return head_photo_for_contact;
    }
    public String getContent_of_contact()
    {
        return content_of_contact;
    }
    public String getTime_of_contact()
    {
        return time_of_contact;
    }
    public String getUsername_for_contact()
    {
        return username_for_contact;
    }

}
