package cn.goal.goal.Services;

import cn.goal.goal.Services.object.DailySentence;
import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class DailySentenceService {
    public static final String apiServer = Config.apiServer;
    public static final String recordUrl = Config.record;

    private static DailySentence today;

    /**
     * 获取今日每日一句信息
     * @return 失败返回null
     */
    public static DailySentence getDailySentenceToday() {
        // 今日未获取
        if (today == null || today.getDate().getDate() != new Date().getDate()) {
            HttpRequest request = HttpRequest
                    .get(apiServer + recordUrl + "/dailySentence/today")
                    .header("Authorization", UserService.getToken());
            if (request.ok()) {
                try {
                    JSONObject result = new JSONObject(request.body());
                    if (result.getInt("code") == 10000) {
                        JSONObject data = result.getJSONObject("data");
                        today = new DailySentence(
                                data.getString("sentence"),
                                data.getString("backImg"),
                                new Date(data.getLong("date"))
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return today;
    }

    public static void logout() {
        today = null;
    }

}
