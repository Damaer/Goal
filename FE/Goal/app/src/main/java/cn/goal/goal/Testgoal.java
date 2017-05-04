package cn.goal.goal;
/**
 * Created by 97617 on 2017/5/4.
 */
public class Testgoal {
    public String goalname;
    public int goalimage;
    public int goaltype;
    public String goaldate;
    public Testgoal(){
        goalname="四级英语考试";
        goaldate="2017年2月30日";
        goalimage=R.drawable.word;
        goaltype=R.drawable.privateicon;
    }
    public String getGoalname()
    {
        return goalname;
    }
    public int getGoaltype()
    {
        return goaltype;
    }
    public String getGoaldate()
    {
        return goaldate;
    }
    public int getGoalimage()
    {
        return goalimage;
    }


}
