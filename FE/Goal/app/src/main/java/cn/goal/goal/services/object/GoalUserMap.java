package cn.goal.goal.services.object;

import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalUserMap {
    private Goal goal;
    private String _id;
    private Boolean isPublic;
    private Date createAt;
    private Date updateAt;
    private Boolean finish;
    private Date begin;
    private Date plan;
    private Date end;
    private int numberOfPeople;

    public GoalUserMap(Goal goal, String _id, Boolean isPublic, Date createAt, Date updateAt, Boolean finish, Date begin, Date plan, Date end, int numberOfPeople) {
        this.goal = goal;
        this._id = _id;
        this.isPublic = isPublic;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.finish = finish;
        this.begin = begin;
        this.plan = plan;
        this.end = end;
        this.numberOfPeople = numberOfPeople;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getPlan() {
        return plan;
    }

    public void setPlan(Date plan) {
        this.plan = plan;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
