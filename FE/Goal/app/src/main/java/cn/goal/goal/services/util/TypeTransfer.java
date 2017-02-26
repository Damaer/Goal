package cn.goal.goal.services.util;

import cn.goal.goal.services.object.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * 封装
 * JSONArray数据转换为ArrayList
 * JSONObject数据转换为对应类型对象
 * 方法
 * Created by chenlin on 25/02/2017.
 */
public class TypeTransfer {
    /**
     * 将JSONArray转换为ArrayList<User>
     * @param data
     * @return
     */
    public static ArrayList<User> getUserArrayFromJSON(JSONArray data) {
        ArrayList<User> users = new ArrayList<>();
        if (data == null) return users;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject user = data.getJSONObject(i);
                users.add(getUserFromJSON(user));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    /**
     * 将JSONObject转换为User
     * @param data
     * @return
     */
    public static User getUserFromJSON(JSONObject data) {
        if (data == null) return null;
        String name = getStringFromJSON(data, "username");
        if (name == null) name = getStringFromJSON(data, "name");
        return new User(
                getStringFromJSON(data, "_id"),
                name,
                getStringFromJSON(data, "avatar"),
                getStringFromJSON(data, "description"),
                getStringFromJSON(data, "email"),
                getStringFromJSON(data, "phone")
        );
    }

    /**
     * 将JSONArray数据转换为ArrayList<Goal>
     * @param data 要转换的数据
     * @return 转换后的ArrayList
     */
    public static ArrayList<Goal> getGoalArrayFromJSON(JSONArray data) {
        ArrayList<Goal> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject goal = data.getJSONObject(i);
                result.add(getGoalFromJSON(goal));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject转换为Goal
     * @param data
     * @return
     */
    public static Goal getGoalFromJSON(JSONObject data) {
        if (data == null) return null;
        return new Goal(
                getStringFromJSON(data, "_id"),
                getStringFromJSON(data, "title"),
                getStringFromJSON(data, "content"),
                getUserFromJSON(getJSONObjectFromJSON(data, "user")),
                new Date(getLongFromJSON(data, "createAt"))
        );
    }

    /**
     * 将JSONArray数据转换为ArrayList<GoalUserMap>
     * @param data 要转换的数据
     * @return
     */
    public static ArrayList<GoalUserMap> getGoalUserMapArrayFromJSON(JSONArray data) {
        ArrayList<GoalUserMap> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject goalUserMap = data.getJSONObject(i);
                result.add(getGoalUserMapFromJSON(goalUserMap));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject数据转换为GoalUserMap
     * @param data
     * @return
     */
    public static GoalUserMap getGoalUserMapFromJSON(JSONObject data) {
        if (data == null) return null;
        return new GoalUserMap(
                getGoalFromJSON(getJSONObjectFromJSON(data, "goal")),
                getStringFromJSON(data, "_id"),
                getBooleanFromJSON(data, "public"),
                new Date(getLongFromJSON(getJSONObjectFromJSON(data, "meta"), "createAt")),
                new Date(getLongFromJSON(getJSONObjectFromJSON(data, "meta"), "updateAt")),
                getBooleanFromJSON(data, "finish"),
                new Date(getLongFromJSON(data, "begin")),
                new Date(getLongFromJSON(data, "plan")),
                new Date(getLongFromJSON(data, "end"))
        );
    }

    /**
     * 将JSONArray转换为ArrayList<GoalsFinishedRecord>
     * @return
     */
    public static ArrayList<GoalsFinishedRecord> getGoalsFinishedRecordArrayFromJSON(JSONArray data) {
        ArrayList<GoalsFinishedRecord> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject goalsFinishedRecord = data.getJSONObject(i);
                result.add(getGoalsFinishedRecordFromJSON(goalsFinishedRecord));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject转换为GoalsFinishedRecord
     * @param data
     * @return
     */
    public static GoalsFinishedRecord getGoalsFinishedRecordFromJSON(JSONObject data) {
        if (data == null) return null;
        return new GoalsFinishedRecord(new Date(getLongFromJSON(data, "date")), getIntFromJSON(data, "nums"));
    }

    /**
     * 将JSONArray转换为ArrayList<Comment>
     * @return
     */
    public static ArrayList<Comment> getCommentArrayFromJSON(JSONArray data) {
        ArrayList<Comment> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject comment = data.getJSONObject(i);
                result.add(getCommentFromJSON(comment));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject转换为Comment
     * @param data
     * @return
     */
    public static Comment getCommentFromJSON(JSONObject data) {
        if (data == null) return null;
        return new Comment(
                getStringFromJSON(data, "content"),
                getUserFromJSON(getJSONObjectFromJSON(data, "user")),
                getStringFromJSON(data, "_id"),
                getIntFromJSON(data, "reply"),
                new Date(getLongFromJSON(data, "createAt")),
                getStringArrayFromJSON(getJSONArrayFromJSON(data, "like"))
        );
    }

    /**
     * 将JSONArray转换为ArrayList<Recommend>
     * @return
     */
    public static ArrayList<Recommend> getRecommendArrayFromJSON(JSONArray data) {
        ArrayList<Recommend> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject comment = data.getJSONObject(i);
                result.add(getRecommendFromJSON(comment));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject转换为Recommend
     * @param data
     * @return
     */
    public static Recommend getRecommendFromJSON(JSONObject data) {
        if (data == null) return null;
        return new Recommend(
                getStringFromJSON(data, "_id"),
                getStringFromJSON(data, "content"),
                getUserFromJSON(getJSONObjectFromJSON(data, "user")),
                new Date(getLongFromJSON(data, "createAt")),
                getGoalFromJSON(getJSONObjectFromJSON(data, "goal")),
                getStringArrayFromJSON(getJSONArrayFromJSON(data, "follower")),
                getStringArrayFromJSON(getJSONArrayFromJSON(data, "like"))
        );
    }

    /**
     * 将JSONArray转换为ArrayList<Message>
     * @return
     */
    public static ArrayList<Message> getMessageArrayFromJSON(JSONArray data) {
        ArrayList<Message> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                JSONObject comment = data.getJSONObject(i);
                result.add(getMessageFromJSON(comment));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将JSONObject转换为Recommend
     * @param data
     * @return
     */
    public static Message getMessageFromJSON(JSONObject data) {
        if (data == null) return null;
        return new Message(
                getUserFromJSON(getJSONObjectFromJSON(data, "sender")),
                getStringFromJSON(data, "content"),
                new Date(getLongFromJSON(data, "createAt")),
                getBooleanFromJSON(data, "hasRead"),
                getStringFromJSON(data, "_id")
        );
    }

    /**
     * 将JSONArray转换为字符串数组
     * @param data
     * @return
     */
    public static ArrayList<String> getStringArrayFromJSON(JSONArray data) {
        ArrayList<String> result = new ArrayList<>();
        if (data == null) return result;
        for (int i = 0; i < data.length(); ++i) {
            try {
                result.add(data.getString(i));
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getStringFromJSON(JSONObject data, String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObjectFromJSON(JSONObject data, String key) {
        try {
            return data.getJSONObject(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getJSONArrayFromJSON(JSONObject data, String key) {
        try {
            return data.getJSONArray(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static long getLongFromJSON(JSONObject data, String key) {
        try {
            return data.getLong(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return 0;
    }

    public static int getIntFromJSON(JSONObject data, String key) {
        try {
            return data.getInt(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return 0;
    }

    public static Boolean getBooleanFromJSON(JSONObject data, String key) {
        try {
            return data.getBoolean(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return false;
    }
}
