package cn.goal.goal.services;

import cn.goal.goal.services.object.Goal;
import cn.goal.goal.services.object.GoalFinished;
import cn.goal.goal.services.object.GoalUserMap;
import cn.goal.goal.services.util.TypeTransfer;
import cn.goal.goal.utils.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenlin on 25/02/2017.
 */
public class GoalUserMapService {
    public static final String apiServer = Config.apiServer;
    public static final String goalMapUrl = Config.goalmap;
    public static final String recordUrl = Config.record;

    public static ArrayList<GoalUserMap> goalUserMaps; // 当前用户关注目标列表
    public static ArrayList<GoalFinished> goalsFinished; // 当天已完成目标

    /**
     * 获取当前用户关注目标列表
     * @return 目标列表 失败返回null
     */
    public static ArrayList<GoalUserMap> getGoals() {
        if (goalUserMaps == null) {
            HttpRequest request = HttpRequest
                    .get(apiServer + goalMapUrl)
                    .header("Authorization", UserService.getToken());
            if (request.ok()) {
                try {
                    JSONObject result = new JSONObject(request.body());
                    if (result.getInt("code") == 10000) {
                        goalUserMaps = TypeTransfer.getGoalUserMapArrayFromJSON(result.getJSONArray("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return goalUserMaps;
    }

    /**
     * 查找goal在ArrayList中的下标
     * @return
     */
    public static String getGoalIndex(GoalUserMap goal) {
        return String.valueOf(goalUserMaps.indexOf(goal));
    }

    /**
     * 通过下标获取goal
     * @param index
     * @return
     */
    public static GoalUserMap getGoal(String index) {
        return goalUserMaps.get(Integer.valueOf(index));
    }

    /**
     * 获取指定用户的目标列表
     * @param userId 要获取的用户的_id值
     * @return
     */
    public static ArrayList<GoalUserMap> getOtherUserGoals(String userId) {
        HttpRequest request = HttpRequest
                .get(apiServer + goalMapUrl + "/user/" + userId);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    return TypeTransfer.getGoalUserMapArrayFromJSON(result.getJSONArray("data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加目标
     * @return 成功返回null, 失败返回错误信息
     */
    public static String addGoal(Goal goal, Date begin, Date plan, Boolean isPublic) {
        HttpRequest request = HttpRequest
                .post(apiServer + goalMapUrl + "/goal/" + goal.get_id())
                .header("Authorization", UserService.getToken())
                .form("begin", begin.getTime())
                .form("plan", begin.getTime())
                .form("isPublic", isPublic);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    if (goalUserMaps == null) goalUserMaps = new ArrayList<>();
                    goalUserMaps.add(new GoalUserMap(
                            goal,
                            result.getString("data"),
                            isPublic,
                            new Date(),
                            new Date(),
                            false,
                            begin,
                            plan,
                            new Date(0)
                    ));
                    return null;
                }
                return result.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
                return "服务器传输数据错误";
            }
        }
        return ("请求失败");
    }

    /**
     * 标记目标已完成
     * @param goalUserMap
     * @return
     */
    public static String markFinished(GoalUserMap goalUserMap) {
        HttpRequest request = HttpRequest
                .post(apiServer + goalMapUrl + "/finish/" + goalUserMap.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalUserMap.setFinish(true);
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
     * 标记目标未完成
     * @param goalUserMap
     * @return
     */
    public static String markUnfinished(GoalUserMap goalUserMap) {
        HttpRequest request = HttpRequest
                .delete(apiServer + goalMapUrl + "/finish/" + goalUserMap.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalUserMap.setFinish(false);
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
     * 更改目标信息
     * @param goalUserMap
     * @param begin
     * @param plan
     * @param isPublic
     * @return
     */
    public static String updateGoal(GoalUserMap goalUserMap, Date begin, Date plan, Boolean isPublic) {
        HttpRequest request = HttpRequest
                .put(apiServer + goalMapUrl + "/" + goalUserMap.get_id())
                .header("Authorization", UserService.getToken())
                .form("begin", begin.getTime())
                .form("plan", begin.getTime())
                .form("isPublic", isPublic);
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalUserMap.setBegin(begin);
                    goalUserMap.setPlan(plan);
                    goalUserMap.setPublic(isPublic);
                    return null;
                }
                return result.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
                return "服务器传输数据错误";
            }
        }
        return ("请求失败");
    }

    /**
     * 删除目标
     * @param goalUserMap
     * @return
     */
    public static String deleteGoal(GoalUserMap goalUserMap) {
        HttpRequest request = HttpRequest
                .delete(apiServer + goalMapUrl + "/" + goalUserMap.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalUserMaps.remove(goalUserMap);
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
     * 标记目标今日已完成
     * @return
     */
    public static String markGoalFinishedToday(GoalUserMap goal) {
        HttpRequest request = HttpRequest
                .post(apiServer + recordUrl + "/goal/" + goal.get_id())
                .header("Authorization", UserService.getToken());
        if (request.ok()) {
            try {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalsFinished.add(new GoalFinished(goal, new Date()));
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
     * 获取今日已完成目标
     * @return
     */
    public static ArrayList<GoalFinished> getGoalsFinished() {
        if (goalsFinished == null) goalsFinished = new ArrayList<>();
        // 判断是否到了第二天
        if (goalsFinished.size() > 0) {
            if (goalsFinished.get(0).getDate().getDate() != new Date().getDate()) {
                goalsFinished = new ArrayList<>(); // 第二天则清空数组;
            }
        }
        return goalsFinished;
    }
}
