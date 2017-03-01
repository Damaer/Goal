package cn.goal.goal.services.object;

import java.util.ArrayList;

/**
 * Created by chenlin on 27/02/2017.
 */
public class UserInfo {
    private String description;
    private int focusTime;
    private int numOfGoal;
    ArrayList<User> followers;

    public UserInfo(String description, int focusTime, int numOfGoal, ArrayList<User> followers) {
        this.description = description;
        this.focusTime = focusTime;
        this.numOfGoal = numOfGoal;
        this.followers = followers;
    }

    public String getDescription() {
        return description;
    }

    public int getFocusTime() {
        return focusTime;
    }

    public int getNumOfGoal() {
        return numOfGoal;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }
}
