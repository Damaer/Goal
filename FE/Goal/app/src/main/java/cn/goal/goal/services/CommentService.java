package cn.goal.goal.services;

import cn.goal.goal.services.object.Comment;
import cn.goal.goal.services.object.Goal;
import cn.goal.goal.services.utils.TypeTransfer;
import cn.goal.goal.utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenlin on 25/02/2017.
 */
public class CommentService {
    public static final String apiServer = Config.apiServer;
    public static final String commentUrl = Config.commentUrl;

    /**
     * 获取指定目标下的所有评论
     * @return 失败返回null;
     */
    public static ArrayList<Comment> getCommentOfGoal(Goal goal) {
        HttpRequest request = HttpRequest
                .get(apiServer + commentUrl + "/goal/" + goal.get_id());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return TypeTransfer.getCommentArrayFromJSON(result.getJSONArray("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 向目标评论
     * @param content 评论内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String comment(Goal goal, String content) {
        HttpRequest request = HttpRequest
                .post(apiServer + commentUrl + "/goal/" + goal.get_id())
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
     * 获取指定评论的回复列表
     * @return 失败返回null;
     */
    public static ArrayList<Comment> getReplyOfComment(Comment comment) {
        HttpRequest request = HttpRequest
                .get(apiServer + commentUrl + "/" + comment.get_id());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return TypeTransfer.getCommentArrayFromJSON(result.getJSONArray("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 点赞
     * @return 失败返回null;
     */
    public static String like(Comment comment) {
        HttpRequest request = HttpRequest
                .post(apiServer + commentUrl + "/like/" + comment.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "服务器传输数据错误";
            }
        }
        return "请求失败";
    }

    /**
     * 点赞
     * @return 失败返回null;
     */
    public static String unlike(Comment comment) {
        HttpRequest request = HttpRequest
                .delete(apiServer + commentUrl + "/like/" + comment.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "服务器传输数据错误";
            }
        }
        return "请求失败";
    }

    /**
     * 回复评论
     * @param content 评论内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String reply(Comment comment, String content) {
        HttpRequest request = HttpRequest
                .post(apiServer + commentUrl + "/" + comment.get_id())
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
     * 删除评论
     * @return 成功返回null, 失败返回错误信息
     */
    public static String reply(Comment comment) {
        HttpRequest request = HttpRequest
                .delete(apiServer + commentUrl + "/" + comment.get_id())
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
