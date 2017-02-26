package cn.goal.goal.services.object;

import java.util.Date;

/**
 * Created by chenlin on 19/02/2017.
 */
public class Goal {
    private String _id;
    private String title;
    private String content;
    private Date createAt;
    private User user;

    public Goal(String _id, String title, String content, User user, Date createAt) {
        this._id = _id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createAt = createAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
