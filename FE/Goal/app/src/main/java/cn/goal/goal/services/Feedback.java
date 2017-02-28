package cn.goal.goal.Services;

import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenlin on 18/02/2017.
 */
public class Feedback {
    /**
     * 提交反馈
     * @param content 反馈内容
     * @param contact 联系方式
     * @return 成功返回null, 错误返回错误信息
     */
    public static final String apiServer = Config.apiServer;
    public static final String feedback = Config.feedback;

    public static String submitFeedback(String content, String contact) {
        // 提交建议
        HttpRequest request = HttpRequest
                .post(apiServer + feedback)
                .form("content", content)
                .form("contact", contact);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return null;
                }
                return result.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
                return "服务器传输数据错误";
            }
        }
        return "请求失败";
    }
}