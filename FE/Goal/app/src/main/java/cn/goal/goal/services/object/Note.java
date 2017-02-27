package cn.goal.goal.Services.object;

/**
 * Created by chenlin on 20/02/2017.
 */
public class Note {
    private int id;
    private String _id;
    private String content;
    private String createAt;
    private String updateAt;

    public Note(int id, String _id, String content, String createAt, String updateAt) {
        this.id = id;
        this._id = _id;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
