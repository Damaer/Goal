package cn.goal.goal.services.object;

/**
 * Created by chenlin on 28/02/2017.
 */
public class Rank {
    private User user;
    private int focusTime;

    public Rank(User user, int focusTime) {
        this.user = user;
        this.focusTime = focusTime;
    }

    public User getUser() {
        return user;
    }

    public int getFocusTime() {
        return focusTime;
    }
}
