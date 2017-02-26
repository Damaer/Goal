package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/21.
 */

public class Goal_record_class {
    private int user_Image;
    private String user_name;
    private String user_record;
    private String time_of_send;
    private String goal_name;
    private int ImageId_btn_like;
    private int sum_of_like;
    private int ImageId_btn_share;
    private int ImageId_btn_reply;
    private int sum_of_reply;
    public Goal_record_class(int user_Image,String user_name,String time_of_send,int sum_of_like)
    {
        this.user_Image=user_Image;
        this.user_name=user_name;
        this.goal_name="goal#学习Android#";
        this.user_record="我们诚惶诚恐，我们小心翼翼，我们毫无保留，却不一定就能把爱，变成相爱。如果这一次真得背过身我们不再遇见了，那我希望我们可以面对面再背对背，永远不留遗憾，一生山高水阔。只是，后来，我们都甘愿被生活凌迟。";
        this.time_of_send=time_of_send;
        this.ImageId_btn_like=R.mipmap.like;
        this.sum_of_like=sum_of_like;
        this.ImageId_btn_reply=R.mipmap.reply;
        this.ImageId_btn_share=R.mipmap.share2;
        this.sum_of_reply=sum_of_like;
    }

    /**
     * get方法的实现
     * @return
     */
    public String getGoal_name(){return goal_name;}
    public int getUser_Image()
    {
        return user_Image;
    }
    public String getUser_name(){
        return user_name;
    }
    public String getUser_record(){
        return user_record;
    }
    public String getTime_of_send()
    {
        return time_of_send;
    }
    public int getImageId_btn_like(){
        return ImageId_btn_like;
    }
    public int getSum_of_like(){
        return sum_of_like;
    }
    public int getImageId_btn_share(){
        return ImageId_btn_share;
    }
    public int getImageId_btn_reply(){
        return ImageId_btn_reply;
    }
    public int getSum_of_reply(){return sum_of_reply;}

}
