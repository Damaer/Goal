package cn.goal.goal.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import cn.goal.goal.services.object.Goal;
import cn.goal.goal.services.object.Note;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.HttpRequest;

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
    private static ArrayList<Note> notes; // 所有标签
    private static ArrayList<Goal> goals; // 所有目标
    private static ArrayList<Integer> goalsFinished; // 当天已完成目标

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
        getLocalNotes(db);
        getLocalGoalsFinished(db);
        db.close();
    }

    public static void removeLocalInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.remove("username");
        editor.remove("avatar");
        editor.remove("description");
        editor.remove("email");
        editor.remove("phone");
        editor.commit();

        SQLiteDatabase db = getDB();
        db.delete("goal", null, null);
        db.delete("note", null, null);
        db.delete("dailySentence", null, null);
        db.delete("goalsFinished", null, null);
        db.close();
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

    private static void getLocalGoalsFinished(SQLiteDatabase db) {
        goalsFinished = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from goalsFinished where date=?", new String[]{dateToString(new Date())});
        int goalIdIndex = cursor.getColumnIndex("goalId");
        while (cursor.moveToNext()) {
            goalsFinished.add(cursor.getInt(goalIdIndex));
        }
    }

    private static Goal queryGoal(int goalId, SQLiteDatabase db) {
        Goal goal = null;
        Cursor cursor = db.rawQuery("select * from goal where id=?", new String[]{String.valueOf(goalId)});
        if (cursor.moveToFirst()) {
            goal = getGoalFromCursor(cursor);
        }
        return goal;
    }

    private static void getLocalNotes(SQLiteDatabase db) {
        notes = new ArrayList<>();
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        int idIndex = cursor.getColumnIndex("id");
        int _idIndex = cursor.getColumnIndex("_id");
        int contentIndex = cursor.getColumnIndex("content");
        int createAtIndex = cursor.getColumnIndex("createAt");
        int updateAtIndex = cursor.getColumnIndex("updateAt");
        while (cursor.moveToNext()) {
            Note note = new Note(
                    cursor.getInt(idIndex),
                    cursor.getString(_idIndex),
                    cursor.getString(contentIndex),
                    cursor.getString(createAtIndex),
                    cursor.getString(updateAtIndex)
            );
            notes.add(note);
        }
        cursor.close();
    }

    private static void getLocalGoals(SQLiteDatabase db) {
        goals = new ArrayList<>();
        Cursor cursor = db.query("goal", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            goals.add(getGoalFromCursor(cursor));
        }
        cursor.close();
    }

    private static Goal getGoalFromCursor(Cursor cursor) {
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
        return new Goal(
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
    }

    private static SQLiteDatabase getDB() {
        return new DBHelper(context, "goal", null, 2).getReadableDatabase();
    }

    /**
     * 用户登录
     * @param account 用户邮箱或手机号
     * @param password 密码
     * @return 登录失败返回错误信息，成功返回null
     */
    public static String login(String account, String password) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + loginUrl)
                    .form("account", account)
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
     * @param account 邮箱或手机号
     * @return 注册成功返回null, 失败返回错误信息
     */
    public static String register(String account, String password) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + registerUrl)
                    .form("account", account)
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
        goalsFinished = null;
    }

    /**
     * 上传头像接口
     * @param avatarImg 要上传的文件
     * @return 成功返回头像Url, 失败返回null
     */
    public static String uploadAvatar(String avatarImg) {
        try {
            HttpRequest request = HttpRequest
                    .post(baseUrl + avatarUrl)
                    .header("Authorization", token)
                    .part("avatar", "avatar", new File(avatarImg));
            JSONObject result = new JSONObject(request.body());
            if (result.getInt("code") == 10000) {
                return result.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                            data.getString("email"),
                            data.getString("phone")
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
    public static String updateUserInfo(String newUsername, String newAvatar, String description) {
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
                    .form("avatar", newAvatar)
                    .form("description", description);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                // 保存用户信息到SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("avatar", newAvatar);
                editor.putString("username", newUsername);
                editor.putString("description", description);
                editor.commit();

                userInfo.setAvatar(newAvatar);
                userInfo.setUsername(newUsername);
                userInfo.setDescription(description);
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
     * @return
     */
    public static ArrayList<Note> getNotes() {
        return notes;
    }

    /**
     * 新建用户标签
     * @param content 标签内容
     * @return 成功返回null, 失败返回错误信息
     */
    public static String createNote(String content) {
        String _id = "null";
        String createAt = dateToString(new Date());

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("_id", _id);
        cv.put("content", content);
        cv.put("createAt", createAt);
        cv.put("updateAt", createAt);
        db.insert("note", null, cv);

        // 获取最后插入的记录id
        Cursor cursor = db.rawQuery("select last_insert_rowid() from note", null);
        int lastId = 0;
        if (cursor.moveToFirst()) lastId = cursor.getInt(0);
        cursor.close();
        db.close();

        if (lastId == 0) return "新建目标失败";
        if (notes == null) notes = new ArrayList<>();

        notes.add(new Note(lastId, _id, content, createAt, createAt));
        return null;
    }

    /**
     * 获取指定id的note信息
     * @param noteId 要获取的noteId
     * @return 成功返回note信息，失败返回null
     */
    public static Note getNote(int noteId) {
        int noteIndex = findNoteById(noteId);
        if (noteIndex == -1) return null;
        return notes.get(noteIndex);
    }

    /**
     * 查找note
     * @param noteId noteId
     * @return noteId在notes数组中的下表Index;
     */
    public static int findNoteById(int noteId) {
        for (int i = 0; i <notes.size(); ++i) {
            if (notes.get(i).getId() == noteId) return i;
        }
        return -1;
    }

    /**
     * 更新标签内容
     * @param note 标签
     * @param content 标签内容
     * @return 更新成功返回null, 失败返回错误信息
     */
    public static String updateNote(Note note, String content) {
        // 本地更新
        String updateAt = dateToString(new Date());
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("updateAt", updateAt);
        db.update("note", cv, "id=?", new String[]{String.valueOf(note.getId())});
        db.close();
        // 更新goals数组信息
        note.setContent(content);
        return null;
    }

    /**
     * 删除指定标签
     * @param note 要删除标签
     * @return 成功返回null, 失败返回错误信息
     */
    public static String deleteNote(Note note) {
        // 本地删除
        SQLiteDatabase db = getDB();
        db.delete("note", "id=?", new String[]{String.valueOf(note.getId())});
        db.close();
        int index = findNoteById(note.getId());
        if (index != -1) {
            notes.remove(index);
        }
        return null;
    }

    /**
     * 获取用户所有目标
     * @return 成功返回JSONArray, 失败返回null
     */
    public static ArrayList<Goal> getGoals() {
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
            goals = new ArrayList<>();
        }
        goals.add(new Goal(
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
    }

    /**
     * 获取指定Id的goal信息
     * @param goalId
     * @return 失败返回null
     */
    public static Goal getGoal(int goalId) {
        int goalIndex = findGoalById(goalId);
        if (goalIndex == -1) return null;
        return goals.get(goalIndex);
    }

    /**
     * 通过goalId 查找goals数组中的index
     * @param goalId 要查找的goalid
     * @return 失败返回-1
     */
    public static int findGoalById(int goalId) {
        if (goals == null) return -1;
        for (int i = 0; i < goals.size(); ++i) {
            if (goals.get(i).getId() == goalId) return i;
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
        db.close();
        int index = findGoalById(goal.getId());
        if (index != -1) {
            goals.remove(index);
        }
        return null;
    }

    public static ArrayList<Integer> getGoalsFinished() {
        return goalsFinished;
    }

    /**
     * 标记目标当天已完成
     * @param goal 已完成目标
     * @return 成功返回null, 失败返回错误信息
     */
    public static String markGoalFinishedToday(Goal goal) {
        if (goalsFinished == null) goalsFinished = new ArrayList<>();
        if (goal.getFinished() == 1) return "已完成目标不可再次标记完成";
        if (goalsFinished.contains(goal.getId())) return "该目标今日已完成！";
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("_id", "null");
        cv.put("date", dateToString(new Date()));
        cv.put("goalId", goal.getId());
        db.insert("goalsFinished", null, cv);
        db.close();
        goalsFinished.add(goal.getId());
        return null;
    }

    /**
     * 标记目标已完成
     * @param goal 已完成目标
     * @return 成功返回Null, 失败返回错误信息
     */
    public static String markGoalFinished(Goal goal) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("finished", 1);
        db.update("goal", cv, "id=?", new String[]{String.valueOf(goal.getId())});
        db.close();
        goal.setFinished(1);
        return null;
    }

    /**
     * 标记目标未完成
     * @param goal 未完成目标
     * @return 成功返回null, 失败返回错误信息
     */
    public static String markGoalUnfinished(Goal goal) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put("finished", 0);
        db.update("goal", cv, "id=?", new String[]{String.valueOf(goal.getId())});
        db.close();
        goal.setFinished(0);
        return null;
    }

    /**
     * 获取当天已完成目标数量
     * @return 失败返回null
     */
    public static Integer getGoalsFinishedNums() {
        return goalsFinished == null ? 0 : goalsFinished.size();
    }
}
