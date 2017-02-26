package cn.goal.goal.services.object;

import java.util.Date;

/**
 * Created by chenlin on 20/02/2017.
 */
public class DailySentence {
    private String sentence;
    private String backImg;
    private Date date;

    public DailySentence(String sentence, String backImg, Date date) {
        this.sentence = sentence;
        this.backImg = backImg;
        this.date = date;
    }

    public String getSentence() {
        return sentence;
    }

    public String getBackImg() {
        return backImg;
    }

    public Date getDate() {
        return date;
    }
}
