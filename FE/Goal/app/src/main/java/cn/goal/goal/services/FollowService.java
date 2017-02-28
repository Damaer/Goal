package cn.goal.goal.Services;

import cn.goal.goal.Services.object.User;
import cn.goal.goal.Services.util.TypeTransfer;
import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenlin on 25/02/2017.
 */
public class FollowService {
    public static final String apiServer = Config.apiServer;
    public static final String followUrl = Config.followUrl;

    /**
     * 获取关注当前用户的人
     * @return 失败返回null
     */
    public static ArrayList<User> getFollowers() {
        HttpRequest request = HttpRequest
                .get(apiServer + followUrl)
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return TypeTransfer.getUserArrayFromJSON(result.getJSONArray("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取关注指定用户的人
     * @return 失败返回null
     */
    public static ArrayList<User> getOtherUserFollowers(User user) {
        HttpRequest request = HttpRequest
                .get(apiServer + followUrl + "/user/" + user.get_id());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return TypeTransfer.getUserArrayFromJSON(result.getJSONArray("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 关注指定用户
     * @return 成功返回null, 失败返回错误信息
     */
    public static String followUser(User user) {
        HttpRequest request = HttpRequest
                .post(apiServer + followUrl + "/user/" + user.get_id())
                .header("Authorization", UserService.getToken());
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

    /**
     * 取消关注指定用户
     * @return 成功返回null, 失败返回错误信息
     */
    public static String unfollowUser(User user) {
        HttpRequest request = HttpRequest
                .delete(apiServer + followUrl + "/user/" + user.get_id())
                .header("Authorization", UserService.getToken());
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
