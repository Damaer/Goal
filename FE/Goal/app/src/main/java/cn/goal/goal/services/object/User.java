package cn.goal.goal.services.object;

/**
 * Created by chenlin on 19/02/2017.
 */
public class User {
    private String username;
    private String avatar;
    private String description;
    private String email;
    private String phone;

    public User(String username, String avatar, String description, String email, String phone) {
        this.username = username;
        this.avatar = avatar;
        this.description = description;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
