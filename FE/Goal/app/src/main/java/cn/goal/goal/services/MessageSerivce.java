package cn.goal.goal.Services;

import cn.goal.goal.Services.object.Message;
import cn.goal.goal.Services.object.User;
import cn.goal.goal.Services.util.TypeTransfer;
import cn.goal.goal.Utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenlin on 26/02/2017.
 */
public class MessageSerivce {
    private static final String apiServer = Config.apiServer;
    private static final String messageUrl = Config.messageUrl;

    private static ArrayList<Message> messageArrayList;

    /**
     * 获取消息列表
     * @return 失败返回null;
     */
    public static ArrayList<Message> getMessages() {
        if (messageArrayList != null) return messageArrayList;
        return refreshAndGetMessages();
    }

    /**
     * 刷新并获取消息列表
     * @return 失败返回null;
     */
    public static ArrayList<Message> refreshAndGetMessages() {
        HttpRequest request = HttpRequest
                .get(apiServer + messageUrl)
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    messageArrayList = TypeTransfer.getMessageArrayFromJSON(result.getJSONArray("data"));
                    return messageArrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送消息
     * @param receiver 接收者
     * @param content 发送内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String sendMessage(User receiver, String content) {
        HttpRequest request = HttpRequest
                .post(apiServer + messageUrl + "/user/" + receiver.get_id())
                .header("Authorization", UserService.getToken())
                .form("content", content);
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
     * 标记消息已读
     * @return 成功返回null, 失败返回错误信息
     */
    public static String markRead(Message message) {
        HttpRequest request = HttpRequest
                .post(apiServer + messageUrl + "/read/" + message.get_id())
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
     * 删除消息
     * @return 成功返回null, 失败返回错误信息
     */
    public static String deleteMessage(Message message) {
        HttpRequest request = HttpRequest
                .delete(apiServer + messageUrl + "/" + message.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    messageArrayList.remove(message);
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
        messageArrayList = null;
    }

}
