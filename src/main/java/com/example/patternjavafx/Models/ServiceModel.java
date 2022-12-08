package com.example.patternjavafx.Models;

import java.util.HashMap;

public class ServiceModel extends UserModel {
    HashMap<String, String> info_about_service;

    public ServiceModel(HashMap<String, String> info_about_service) {
        super(info_about_service);
        this.info_about_service = info_about_service;
    }

    public String getId() {
        return info_about_service.get("id");
    }

    public String getName() {
        return info_about_service.get("name");
    }

    public String getPrice() {
        return info_about_service.get("price");
    }

}
