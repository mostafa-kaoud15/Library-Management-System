package model;

import enums.UserRole;

public class User {
    private int id;
    private String username;
    private String password;
    private UserRole userRole;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public User(int id, String username, String password, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userRole = UserRole.fromString(userRole);
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.userRole = UserRole.fromString(role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        if (userRole == null) {
            return null;
        }
        return userRole.toString();
    }

    public void setUserRole(String userRole) {
        this.userRole = UserRole.fromString(userRole);
    }
}