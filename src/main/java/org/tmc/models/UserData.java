package org.tmc.models;

public class UserData {

    private int userId;
    private String username;
    private String email;
    private int userDisabled;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserDisabled() {
        return userDisabled;
    }

    public void setUserDisabled(int userDisabled) {
        this.userDisabled = userDisabled;
    }
}
