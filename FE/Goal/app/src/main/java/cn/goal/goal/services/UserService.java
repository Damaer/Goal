package cn.goal.goal.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import cn.goal.goal.services.object.UserInfo;
import cn.goal.goal.services.utils.TypeTransfer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import cn.goal.goal.services.object.User;
import cn.goal.goal.utils.HttpRequest;

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

    private static String token;
    private static User userInfo;

    private static SharedPreferences sp;
    private static Context context;
    private static SQLiteDatabase db;

    /**
     * 启动应用首先调用该方法初始化本地数据
     */
    public static void initData(SharedPreferences mSharedPreferences, Context mContext) {
        // 获取已登录账号token
        sp = mSharedPreferences;
        context = mContext;
        token = sp.getString("token", null);
        // 如果没有登录账号则直接返回
        if (token == null) return;

        getUserInfo(); // 获取本地用户信息
        NoteService.getLocalNotes(getDB());
    }

    public static void removeLocalInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("_id");
        editor.remove("token");
        editor.remove("username");
        editor.remove("avatar");
        editor.remove("description");
        editor.remove("email");
        editor.remove("phone");
        editor.remove("focus_time");
        editor.commit();

        // 清空Service现有信息
        GoalService.logout();
        RecommendService.logout();
        GoalUserMapService.logout();
        FocusTimeService.logout();
        DailySentenceService.logout();
    }

    public static User getUserInfo() {
        if (userInfo == null) {
            userInfo = new User(
                    sp.getString("_id", null),
                    sp.getString("username", null),
                    sp.getString("avatar", null),
                    sp.getString("description", null),
                    sp.getString("email", null),
                    sp.getString("phone", null)
            );
        }
        return userInfo;
    }

    public static SQLiteDatabase getDB() {
        return new DBHelper(context, "goal", null, 2).getReadableDatabase();
    }

    /**
     * 用户登录
     *
     * @param account  用户邮箱或手机号
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
                userInfo = TypeTransfer.getUserFromJSON(data);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("_id", userInfo.get_id());
                editor.putString("token", token);
                editor.putString("username", userInfo.getUsername());
                editor.putString("avatar", userInfo.getAvatar());
                editor.putString("description", userInfo.getDescription());
                editor.putString("email", userInfo.getEmail());
                editor.putString("phone", userInfo.getPhone());
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
     *
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
    }

    /**
     * 上传头像接口
     *
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
     * 获取服务器上的用户信息并判断登录信息是否过期
     * 登录信息过期返回true,否则返回false
     * @return
     */
    public static Boolean fetchUserInfo() {
        if (userInfo == null) return true;
        try {
            HttpRequest request = HttpRequest
                    .get(baseUrl + userInfoUrl)
                    .header("Authorization", token);
            JSONObject result = new JSONObject(request.body());

            if (result.getInt("code") == 10000) {
                JSONObject data = result.getJSONObject("data");
                // 更新当前用户信息
                userInfo.setUsername(data.getString("username"));
                userInfo.setAvatar(data.getString("avatar"));
                userInfo.setEmail(data.getString("email"));
                userInfo.setPhone(data.getString("phone"));
                userInfo.setDescription(data.getString("description"));

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", userInfo.getUsername());
                editor.putString("avatar", userInfo.getAvatar());
                editor.putString("description", userInfo.getDescription());
                editor.putString("email", userInfo.getEmail());
                editor.putString("phone", userInfo.getPhone());
                editor.commit();
            } else if (result.getInt("code") == 10402) {
                // 登录信息过期
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新用户基本信息
     *
     * @param newUsername 新用户名，为空则不更新
     * @param newAvatar   新用户头像地址，为空则不更新
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
     *
     * @param oldPassword 用户当前密码
     * @param password    用户新密码
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
     *
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
                // 保存用户信息到SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", email);
                editor.commit();
                userInfo.setEmail(email);
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
     *
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
                // 保存用户信息到SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone", phone);
                editor.commit();
                userInfo.setEmail(phone);
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
     * 获取其他用户信息
     * 包括个人描述、专注时长、关注他的人、目标数量
     * @param user
     * @return
     */
    public static UserInfo getOtherUserInfo(User user) {
        try {
            HttpRequest request = HttpRequest
                    .get(baseUrl + userInfoUrl + "/" + user.get_id());
            if (request.ok()) {
                JSONObject result = new JSONObject(request.body());
                if (result.getInt("code") == 10000) {
                    JSONObject data = result.getJSONObject("data");
                    return new UserInfo(
                            data.getString("description"),
                            data.getInt("focusTime"),
                            data.getInt("numOfGoal"),
                            TypeTransfer.getUserArrayFromJSON(data.getJSONArray("followers"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}