package cn.goal.goal.services.object;

/**
 * Created by chenlin on 20/02/2017.
 */
public class DailySentence {
    private String sentence;
    private String backImg;

    public DailySentence(String sentence, String backImg) {
        this.sentence = sentence;
        this.backImg = backImg;
    }

    public String getSentence() {
        return sentence;
    }

    public String getBackImg() {
        return backImg;
    }
}
