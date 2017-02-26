package cn.goal.goal.services.object;

import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalFinished {
    private GoalUserMap goal;
    private Date date;

    public GoalFinished(GoalUserMap goal, Date date) {
        this.goal = goal;
        this.date = date;
    }

    public GoalUserMap getGoal() {
        return goal;
    }

    public void setGoal(GoalUserMap goal) {
        this.goal = goal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
