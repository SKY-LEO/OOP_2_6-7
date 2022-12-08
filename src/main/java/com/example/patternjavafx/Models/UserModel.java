package com.example.patternjavafx.Models;

import java.util.HashMap;

public class UserModel extends OrderModel {
    HashMap<String, String> info_about_user;

    public UserModel(HashMap<String, String> info_about_user) {
        super(info_about_user);
        this.info_about_user = info_about_user;
    }

    public String getId() {
        return info_about_user.get("id");
    }

    public String getLogin() {
        return info_about_user.get("login");
    }

    public String getPassword() {
        return info_about_user.get("password");
    }

    public String getSalt() {
        return info_about_user.get("salt");
    }

    public String getRole() {
        return info_about_user.get("role");
    }

    public String getFIO() {
        return info_about_user.get("FIO");
    }

    public String getIs_update() {
        return info_about_user.get("is_update");
    }
}
