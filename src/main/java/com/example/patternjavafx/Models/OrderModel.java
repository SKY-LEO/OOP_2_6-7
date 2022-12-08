package com.example.patternjavafx.Models;

import java.util.HashMap;

public class OrderModel {
    HashMap<String, String> info_about_order;

    public OrderModel(HashMap<String, String> info_about_order) {
        this.info_about_order = info_about_order;
    }

    public String getId() {
        return info_about_order.get("id");
    }

    public String getAccount_id() {
        return info_about_order.get("account_id");
    }

    public String getService_id() {
        return info_about_order.get("service_id");
    }

}
