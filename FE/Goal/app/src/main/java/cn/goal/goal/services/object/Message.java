package cn.goal.goal.Services.object;

import java.util.Date;

/**
 * Created by chenlin on 26/02/2017.
 */
public class Message {
    private User sender;
    private String content;
    private Date createAt;
    private Boolean hasRead;
    private String _id;

    public Message(User sender, String content, Date createAt, Boolean hasRead, String _id) {
        this.sender = sender;
        this.content = content;
        this.createAt = createAt;
        this.hasRead = hasRead;
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Boolean getHasRead() {
        return hasRead;
    }
}
