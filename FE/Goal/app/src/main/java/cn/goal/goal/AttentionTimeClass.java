package cn.goal.goal;

/**
 * Created by 97617 on 2017/5/10.
 */

public class AttentionTimeClass {
    public int minute;
    public int second;
    public AttentionTimeClass(String time)
    {
        minute=Integer.valueOf(new StringBuffer(time).substring(0,time.indexOf("分")));
        second=0;
    }
    public String gettime(){
        if(minute<10&&second>=10)
            return "0"+minute+":"+second;
        else if(minute>=10&&second<10)
            return minute+":0"+second;
        else if(minute<10&&second<10)
            return "0"+minute+":0"+second;
        else
            return minute+":"+second;
    }
    public int reduce() {
        if(minute==0&&second==0)
            return 0;
        else {
            if (minute != 0 && second == 0) {
                minute--;
                second = 59;
            } else {
                second--;
            }
            return 1;
        }
    }

}
