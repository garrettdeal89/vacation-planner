package com.example.d308_vacation_planner.UI;

public class Users {

    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userConfirmPassword;

    public Users(int userId, String userName, String userEmail, String userPassword, String userConfirmPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userConfirmPassword = userConfirmPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserConfirmPassword() {
        return userConfirmPassword;
    }

    public void setUserConfirmPassword(String userConfirmPassword) {
        this.userConfirmPassword = userConfirmPassword;
    }
}
