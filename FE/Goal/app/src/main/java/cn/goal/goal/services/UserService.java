package cn.goal.goal.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import cn.goal.goal.services.object.Goal;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.Date;

import static cn.goal.goal.utils.Util.dateToString;

/**
 * Created by chenlin on 15/02/2017.
 * 用户相关 http 请求
 */
public class UserService {
    public static final String baseUrl = Config.apiServer;
    public static final String loginUrl = Config.login;
    public static final String registerUrl = Config.register;
    public static final String logoutUrl = Config.logout;
    public static final String avatarUrl = Config.avatar;
    public static final String userInfoUrl = Config.userInfo;
    public static final String passwordUrl = Config.password;
    public static final String emailUrl = Config.email;
    public static final String phoneUrl = Config.phone;
    public static final String noteUrl = Config.note;
    public static final String goalUrl  =Config.goal;
    public static final String recordUrl = Config.record;
    public static final String recordTodayUrl = Config.recordToday;
    public static final String goalsFinishedUrl = Config.goalsFinished;

    private static String token;
    private static User userInfo;
    private static JSONArray notes;
    private static JSONArray goals;
    private static JSONArray records;
    private static JSONArray goalsFinished;

    private static SharedPreferences sp;
    private static Context context;

    /**
     * 启动应用首先调用该方法初始化本地数据
     */
    public static void initData(SharedPreferences mSharedPreferences, Context mContext) {
        // 获取已登录账号token
        sp = mSharedPreferences;
        context = mContext;
        token = sp.getString("token", null);
        // 如果没有登录账号则直接返回
        if (token == null) return ;

        getLocalUserInfo(); // 获取本地用户信息

        SQLiteDatabase db = getDB();

        getLocalGoals(db);

        db.close();

//        getNotes(); // 获取notes
//        getGoals(); // 获取goals
//        getRecords(); //获取records
//        getGoalsFinished(); // 获取已完成目标
    }

    private static void getLocalUserInfo() {
        if (userInfo == null) {
            userInfo = new User(
                    sp.getString("username", null),
                    sp.getString("avatar", null),
                    sp.getString("description", null),
                    sp.getString("email", null),
                    sp.getString("phone", null)
            );
        }
    }

    private static void getLocalGoals(SQLiteDatabase db) {
        goals = new JSONArray();
        Cursor cursor = db.query("goal", null, null, null, null, null, null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex("id");
        int _idIndex = cursor.getColumnIndex("_id");
        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");
        int beginIndex = cursor.getColumnIndex("begin");
        int planIndex = cursor.getColumnIndex("plan");
        int endIndex = cursor.getColumnIndex("end");
        int createAtIndex = cursor.getColumnIndex("createAt");
        int updateAtIndex = cursor.getColumnIndex("updateAt");
        int finishedIndex = cursor.getColumnIndex("finished");
        while (cursor.moveToNext()) {
            Goal goal = new Goal(
                    cursor.getShort(idIndex),
                    cursor.getString(_idIndex),
                    cursor.getString(titleIndex),
                    cursor.getString(contentIndex),
                    cursor.getString(beginIndex),
                    cursor.getString(endIndex),
                    cursor.getString(planIndex),
                    cursor.getString(createAtIndex),
                    cursor.getString(updateAtIndex),
                    cursor.getInt(finishedIndex)
            );
            goals.put(goal);
        }
        cursor.close();
    }

    private static SQLiteDatabase getDB() {
        return new DBHelper(context, "goal", null, 2).getReadableDatabase();
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录失败返回错误信息，成功返回null
     */
    public static String login(String username, String password) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + loginUrl)
                    .form("username", username)
                    .form("password", password);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                token = request.header("authorization");
                JSONObject data = result.getJSONObject("data");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", token);
                editor.putString("username", data.getString("username"));
                editor.putString("avatar", data.getString("avatar"));
                editor.putString("description", data.getString("description"));
                editor.commit();

                initData(sp, context);
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
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
    public static String register(String username, String password) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + registerUrl)
                    .form("name", username)
                    .form("password", password);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 退出账号
     */
    public static void logout() {
        HttpRequest request = HttpRequest.get(baseUrl + logoutUrl).header("Authorization", token);
        request.body();

        token = null;
        userInfo = null;
        notes = null;
        goals = null;
        records = null;
        goalsFinished = null;
    }

    public static String uploadAvatar(File avatarImg) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + avatarUrl)
                    .header("Authorization", token)
                    .part("avatar", avatarImg);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 获取用户信息
     * 获取失败返回Null,获取成功返回用户信息HashMap
     * @return
     */
    public static User getUserInfo() {
        if (userInfo == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + userInfoUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());

                if (result.getInt("code") == 10000) {
                    JSONObject data = result.getJSONObject("data");
                    userInfo = new User(
                            data.getString("username"),
                            data.getString("avatar"),
                            data.getString("description"),
                            null,
                            null
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return userInfo;
    }


    /**
     * 更新用户基本信息
     * @param newUsername 新用户名，为空则不更新
     * @param newAvatar 新用户头像地址，为空则不更新
     * @return 更新成功返回null, 更新失败返回错误信息
     */
    @Nullable
    public static String updateUserInfo(String newUsername, String newAvatar) {
        if (userInfo == null) {
            return "请先登录";
        }

        newUsername = newUsername == null ? userInfo.getUsername() : newUsername;
        newAvatar = newAvatar == null ? userInfo.getAvatar() : newAvatar;

        try {
            HttpRequest request = HttpRequest
                    .put(baseUrl + userInfoUrl)
                    .header("Authorization", token)
                    .form("name", newUsername)
                    .form("avatar", newAvatar);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 更改用户密码
     * @param oldPassword 用户当前密码
     * @param password 用户新密码
     * @return 修改成功返回null, 失败返回错误信息
     */
    public static String updatePassword(String oldPassword, String password) {
        try {
            HttpRequest request = HttpRequest
                    .put(baseUrl + passwordUrl)
                    .header("Authorization", token)
                    .form("oldPassword", oldPassword)
                    .form("password", password);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 更新用户email
     * @param email 新邮箱
     * @return 成功返回null, 失败返回错误信息
     */
    public static String updateEmail(String email) {
        try {
            HttpRequest request = HttpRequest
                    .put(baseUrl + emailUrl)
                    .header("Authorization", token)
                    .form("email", email);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 更新用户手机号
     * @param phone 新手机号
     * @return 成功返回null, 失败返回错误信息
     */
    public static String updatePhone(String phone) {
        try {
            HttpRequest request = HttpRequest
                    .put(baseUrl + phoneUrl)
                    .header("Authorization", token)
                    .form("phone", phone);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 获取用户所有标签信息
     * 成功返回notes数据，失败返回null
     * @return
     */
    public static JSONArray getNotes() {
        if (notes == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + noteUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    notes = result.getJSONArray("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notes;
    }

    /**
     * 新建用户标签
     * @param content 标签内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String createNote(String content) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + noteUrl)
                    .header("Authorization", token)
                    .form("content", content);
            JSONObject result = new JSONObject(request.body());
            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 获取指定id的note信息
     * @param noteId 要获取的noteId
     * @return 成功返回note信息，失败返回null
     */
    public static JSONObject getNote(String noteId) {
        JSONObject note = getNoteFromNotes(noteId);
        if (note == null) {
            try {
                HttpRequest request =  HttpRequest
                        .get(baseUrl + noteUrl + "/" + noteId)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    note = result.getJSONObject("data");
                    // 添加进现有notes
                    notes.put(note);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return note;
    }

    /**
     * 从现有notes中根据noteId查找note
     * @return 成功返回note， 失败返回null
     */
    private static JSONObject getNoteFromNotes(String noteId) {
        for (int i = 0; i < notes.length(); ++i) {
            try {
                JSONObject note = notes.getJSONObject(i);
                if (note.getString("_id").equals(noteId)) {
                    return note;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 更新标签内容
     * @param noteId 标签Id
     * @param content 标签内容
     * @return 更新成功返回null, 失败返回错误信息
     */
    public static String updateNote(String noteId, String content) {
        try {
            HttpRequest request = HttpRequest
                    .put(baseUrl + noteUrl + "/" + noteId)
                    .header("Authorization", token)
                    .form("content", content);
            JSONObject result = new JSONObject(request.body());
            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 删除指定标签
     * @param noteId 要删除标签Id
     * @return 成功返回null, 失败返回错误信息
     */
    public static String deleteNote(String noteId) {
        try {
            HttpRequest request = HttpRequest
                    .delete(baseUrl + noteUrl + "/" + noteId)
                    .header("Authorization", token);
            JSONObject result = new JSONObject(request.body());
            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 获取用户所有目标
     * @return 成功返回JSONArray, 失败返回null
     */
    public static JSONArray getGoals() {
        if (goals == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + goalUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goals = result.getJSONArray("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return goals;
    }

    /**
     * 新建用户目标
     * @param title 目标标题
     * @param content 目标内容
     * @param begin 目标计划开始时间
     * @param plan 目标计划结束时间
     * @return 成功返回Null, 失败返回错误信息
     */
    public static String createGoal(String title, String content, String begin, String plan) {
        // 本地创建目标
        String _id = "null";
        String end = "1900-1-1";
        String createAt = dateToString(new Date());

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("_id", _id);
        cv.put("title", title);
        cv.put("content", content);
        cv.put("begin", begin);
        cv.put("plan", plan);
        cv.put("end", end);
        cv.put("createAt", createAt);
        cv.put("updateAt", createAt);
        cv.put("finished", 0);
        db.insert("goal", null, cv);

        // 获取最后插入的记录id
        Cursor cursor = db.rawQuery("select last_insert_rowid() from goal", null);
        int lastId = 0;
        if (cursor.moveToFirst()) lastId = cursor.getInt(0);
        cursor.close();
        db.close();

        if (lastId == 0) {
            return "新建目标失败";
        }

        if (goals == null) {
            goals = new JSONArray();
        }
        goals.put(new Goal(
                lastId,
                _id,
                title,
                content,
                begin,
                end,
                plan,
                createAt,
                createAt,
                0
        ));
        return null;
//        try {
//            HttpRequest request = HttpRequest
//                    .post(baseUrl + goalUrl)
//                    .header("Authorization", token)
//                    .form("title", title)
//                    .form("content", content)
//                    .form("begin", begin)
//                    .form("plan", plan);
//            JSONObject result = new JSONObject(request.body());
//            if (result.getInt("code") == 10000) {
//                return null;
//            }
//            return result.getString("msg");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "服务器传输数据错误";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "请检查网络设置";
//        }
    }

    /**
     * 获取指定Id的goal信息
     * @param goalId
     * @return 失败返回null
     */
    public static Goal getGoal(int goalId) {
        Goal goal = getGoalFromGoals(goalId);
//        if (goal == null) {
//            try {
//                HttpRequest request = HttpRequest
//                        .get(baseUrl + goalUrl + "/" + goalId)
//                        .header("Authorization", token);
//                JSONObject result = new JSONObject(request.body());
//                if (result.getInt("code") == 10000) {
//                    goal = result.getJSONObject("data");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return goal;
    }

    /**
     * 从现有goals中查找id为goalId的目标
     * @param goalId 要查找的goalId
     * @return 失败返回null
     */
    private static Goal getGoalFromGoals(int goalId) {
        int index = findGoalById(goalId);
        try {
            if (index != -1) return (Goal) goals.get(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过goalId 查找goals数组中的index
     * @param goalId 要查找的goalid
     * @return 失败返回-1
     */
    private static int findGoalById(int goalId) {
        if (goals == null) return -1;
        for (int i = 0; i < goals.length(); ++i) {
            try {
                Goal goal = (Goal) goals.get(i);
                if (goal.getId() == goalId) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 更新指定目标信息
     * @param goal 要更新的目标
     * @param title 目标标题
     * @param content 目标内容
     * @param begin 目标计划开始时间
     * @param plan 目标计划结束时间
     * @return 成功返回null,失败返回错误信息
     */
    public static String updateGoal(Goal goal, String title, String content, String begin, String plan) {
        // 本地更新
        String updateAt = dateToString(new Date());
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("begin", begin);
        cv.put("plan", plan);
        cv.put("updateAt", updateAt);
        db.update("goal", cv, "id=?", new String[]{String.valueOf(goal.getId())});
        db.close();
        // 更新goals数组信息
        goal.setTitle(title);
        goal.setContent(content);
        goal.setBegin(begin);
        goal.setPlan(plan);
        goal.setUpdateAt(updateAt);

        return null;
//        try {
//            HttpRequest request = HttpRequest
//                    .put(baseUrl + goalUrl + '/' + goalId)
//                    .header("Authorization", token)
//                    .form("title", title)
//                    .form("content", content)
//                    .form("begin", begin)
//                    .form("plan", plan);
//            JSONObject result = new JSONObject(request.body());
//            if (result.getInt("code") == 10000) {
//                return null;
//            }
//            return result.getString("msg");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "服务器传输数据错误";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "请检查网络设置";
//        }
    }

    /**
     * 删除goal
     * @param goal 要删除目标
     * @return 成功返回Null,失败返回错误信息
     */
    public static String deleteGoal(Goal goal) {
        // 本地删除
        SQLiteDatabase db = getDB();
        db.delete("goal", "id=?", new String[]{String.valueOf(goal.getId())});
        int index = findGoalById(goal.getId());
        if (index != -1) {
            goals.remove(index);
        }
//        try {
//            HttpRequest request = HttpRequest
//                    .delete(baseUrl + goalUrl + '/' + goalId)
//                    .header("Authorization", token);
//            JSONObject result = new JSONObject(request.body());
//            if (result.getInt("code") != 10000) {
//                return result.getString("msg");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    /**
     * 获取用户所有记录信息
     * @return 失败返回null
     */
    public static JSONArray getRecords() {
        if (records == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + recordUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    records = result.getJSONArray("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return records;
    }

    /**
     * 获取用户当天记录
     * @return 失败返回null
     */
    public static JSONObject getRecordToday() {
        JSONObject recordToday = getRecordTodayFromRecords();
        if (recordToday == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + recordTodayUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    recordToday = result.getJSONObject("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recordToday;
    }

    /**
     * 从现有records中获取当天record
     * @return
     */
    private static JSONObject getRecordTodayFromRecords() {
        if (records == null) return null;

        for (int i = 0; i < records.length(); ++i) {
            try {
                JSONObject record = records.getJSONObject(i);
                if (record.getLong("date") == new Date().getTime()) {
                    return record;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取用户当天每日一句
     * @return 失败返回null
     */
    public static JSONObject getDailySentence() {
        JSONObject recordToday = getRecordToday();

        try {
            if (recordToday != null) {
                return recordToday.getJSONObject("dailySentence");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getGoalsFinished() {
        if (goalsFinished == null) {
            try {
                HttpRequest request = HttpRequest
                        .get(baseUrl + goalsFinishedUrl)
                        .header("Authorization", token);
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    goalsFinished = result.getJSONArray("data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return goalsFinished;
    }

    /**
     * 标记目标当天已完成
     * @param goal 已完成目标
     * @return 成功返回null, 失败返回错误信息
     */
    public static String markGoalFinishedToday(Goal goal) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + goalsFinishedUrl)
                    .header("Authorization", token)
                    .form("goalId", goal.getId());
            JSONObject result = new JSONObject(request.body());
            if (result.getInt("code") == 10000) {
                return null;
            }
            return result.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            return "服务器传输数据错误";
        } catch (Exception e) {
            e.printStackTrace();
            return "请检查网络设置";
        }
    }

    /**
     * 标记目标已完成
     * @param goal 已完成目标
     * @return 成功返回Null, 失败返回错误信息
     */
    public static String markGoalFinished(Goal goal) {
        return null;
    }

    /**
     * 标记目标未完成
     * @param goal 未完成目标
     * @return 成功返回null, 失败返回错误信息
     */
    public static String markGoalUnfinished(Goal goal) {
        return null;
    }

    /**
     * 获取当天已完成目标数量
     * @return 失败返回null
     */
    public static Integer getGoalsFinishedNums() {
        if (goalsFinished == null) {
            getGoalsFinished();
        }
        if (goalsFinished == null) {
            return null;
        }
        return goalsFinished.length();
    }
}
