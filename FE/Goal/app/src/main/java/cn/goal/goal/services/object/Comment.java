package cn.goal.goal.Services.object;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class Comment {
    private String content;
    private User user;
    private String _id;
    private int numOfReply;
    private Date createAt;
    private ArrayList<String> like;
    public Goal goal;

    public Comment(String content, User user, String _id, int numOfReply, Date createAt, ArrayList<String> like) {
        this.content = content;
        this.user = user;
        this._id = _id;
        this.numOfReply = numOfReply;
        this.createAt = createAt;
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

    public int getNumOfReply() {
        return numOfReply;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public ArrayList<String> getLike() {
        return like;
    }
}
