package cn.goal.goal.services.object;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalFinished {
    private ArrayList<String> goal;
    private Date date;

    public GoalFinished(ArrayList<String> goal, Date date) {
        this.goal = goal;
        this.date = date;
    }

    public void addGoalFinished(String goal) {
        if (this.goal == null) this.goal = new ArrayList<>();
        this.goal.add(goal);
    }

    public ArrayList<String> getGoal() {
        return goal;
    }

    public void setGoal(ArrayList<String> goal) {
        this.goal = goal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
