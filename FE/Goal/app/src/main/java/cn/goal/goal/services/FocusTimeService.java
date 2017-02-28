package cn.goal.goal.Services;

import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class FocusTimeService {
    public static final String apiServer = Config.apiServer;
    public static final String focusTimeUrl = Config.focusTime;

    public static Integer focusTime;

    /**
     * 获取专注时长
     * @return 失败返回null
     */
    public static Integer getFocusTime() {
        if (focusTime == null) {
            HttpRequest request = HttpRequest
                    .get(apiServer + focusTimeUrl)
                    .header("Authorization", UserService.getToken());
            if (request.ok()) {
                try {
                    JSONObject result = new JSONObject(request.body());
                    if (result.getInt("code") == 10000) {
                        focusTime = result.getInt("data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return focusTime;
    }

    /**
     * 增长专注时长
     * @param begin 开始时间
     * @param length 持续时间(分钟)
     * @return 成功返回null, 失败返回错误信息
     */
    public static String addFocusTime(Date begin, int length) {
        HttpRequest request = HttpRequest
                .post(apiServer + focusTimeUrl)
                .header("Authorization", UserService.getToken())
                .form("begin", begin.getTime())
                .form("length", length);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    if (focusTime != null)
                        focusTime += length;
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

    public static void logout() {
        focusTime = null;
    }
}
