package cn.goal.goal.services.object;

import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalsFinishedRecord {
    private Date date;
    private int numOfGoalFinished;

    public GoalsFinishedRecord(Date date, int numOfGoalFinished) {
        this.date = date;
        this.numOfGoalFinished = numOfGoalFinished;
    }

    public Date getDate() {
        return date;
    }

    public int getNumOfGoalFinished() {
        return numOfGoalFinished;
    }
}
