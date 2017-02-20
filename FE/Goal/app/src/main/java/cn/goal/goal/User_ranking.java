package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/20.
 */

public class User_ranking {
    private int rank;
    private int ImageId;
    private String username;
    private float goalvalue;
    public User_ranking(int rank,int ImageId,String username,float goalvalue)
    {
        this.goalvalue=goalvalue;
        this.ImageId=ImageId;
        this.username=username;
        this.rank=rank;
    }
    public int getrank()
    {
        return rank;
    }
    public int getImageId(){
        return ImageId;
    }
    public String getusername(){
        return username;
    }
    public float getGoalvalue()
    {
        return goalvalue;
    }



}
