package cn.goal.goal.services;

import cn.goal.goal.utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by chenlin on 15/02/2017.
 * 用户相关 http 请求
 */
public class User {
    public static final String baseUrl = Config.apiServer;
    public static final String login = Config.login;
    public static final String register = Config.register;

    private static String token;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录失败返回错误信息，成功返回null
     */
    public static String login(String username, String password) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        HttpRequest request = HttpRequest.post(baseUrl + login).form(params);
        JSONObject result = new JSONObject(request.body());

        if (result.getInt("code") == 10000) {
            token = request.header("authorization");
            System.out.println(token);
            return null;
        }
        return result.getString("msg");
    }

    /**
     * 获取用户token
     */
    public static String getToken() {
        return token;
    }

    /**
     * 用户注册
     * @return 注册成功返回null, 失败返回错误信息
     */
    public static String register(String username, String password) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", username);
        params.put("password", password);

        HttpRequest request = HttpRequest.post(baseUrl + register).form(params);
        JSONObject result = new JSONObject(request.body());

        if (result.getInt("code") == 10000) {
            return null;
        }
        return result.getString("msg");
    }
}
