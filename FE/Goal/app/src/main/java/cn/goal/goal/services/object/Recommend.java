package cn.goal.goal.Services.object;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenlin on 26/02/2017.
 */
public class Recommend {
    private String content; // 推荐内容
    private User user;
    private String _id;
    private Date createAt;
    private Goal goal;
    private ArrayList<String> follower;
    private ArrayList<String> like;

    public Recommend(String _id, String content, User user, Date createAt, Goal goal, ArrayList<String> follower, ArrayList<String> like) {
        this._id = _id;
        this.content = content;
        this.user = user;
        this.createAt = createAt;
        this.goal = goal;
        this.follower = follower;
        this.like = like;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public String get_id() {
        return _id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Goal getGoal() {
        return goal;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public ArrayList<String> getLike() {
        return like;
    }
}
