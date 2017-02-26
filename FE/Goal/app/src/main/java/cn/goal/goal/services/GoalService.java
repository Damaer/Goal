package cn.goal.goal.services;

import cn.goal.goal.services.object.Goal;
import cn.goal.goal.services.util.TypeTransfer;
import cn.goal.goal.utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalService {
    public static final String apiServer = Config.apiServer;
    public static final String goalUrl = Config.goal;

    public static ArrayList<Goal> goals;

    /**
     * 获取现有目标列表 (注：不是用户关注的目标列表)
     * @return 失败返回null;
     */
    public static ArrayList<Goal> getGoals() {
        if (goals == null) {
            // 从服务器上获取目标列表
            HttpRequest request = HttpRequest
                    .get(apiServer + goalUrl);
            if (request.ok()) {
                try {
                    JSONObject result = new JSONObject(request.body());
                    if (result.getInt("code") == 10000) {
                        goals = TypeTransfer.getGoalArrayFromJSON(result.getJSONArray("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return goals;
    }

    /**
     * 添加目标到现有目标列表
     * @param title 目标标题
     * @param content 目标内容
     * @return 成功返回添加的目标，失败返回null
     */
    public static Goal addGoal(String title, String content) {
        HttpRequest request = HttpRequest
                .post(apiServer + goalUrl)
                .header("Authorization", UserService.getToken())
                .form("title", title)
                .form("content", content);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return new Goal(
                            result.getString("data"),
                            title,
                            content,
                            UserService.getUserInfo(),
                            new Date()
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
