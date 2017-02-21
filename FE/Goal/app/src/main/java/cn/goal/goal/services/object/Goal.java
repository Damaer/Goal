package cn.goal.goal.services.object;

import java.io.Serializable;

/**
 * Created by chenlin on 19/02/2017.
 */
public class Goal {
    private int id;
    private String _id;
    private String title;
    private String content;
    private String begin;
    private String end;
    private String plan;
    private String createAt;
    private String updateAt;
    private int finished;

    public Goal(int id, String _id, String title, String content, String begin, String end, String plan, String createAt, String updateAt, int finished) {
        this.id = id;
        this._id = _id;
        this.title = title;
        this.content = content;
        this.begin = begin;
        this.end = end;
        this.plan = plan;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getPlan() {
        return plan;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public int getFinished() {
        return finished;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
