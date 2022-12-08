package com.example.patternjavafx.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class MainModel {
    DatabaseModel databaseModel = new DatabaseModel();

    UserModel user;

    public int checkLoginPassword(String login, String password) throws NoSuchAlgorithmException {
        int code;
        ResultSet resultSet = findUserInDB(login);
        try {
            if (resultSet.next()) {
                //TODO constants
                String salted_password;
                String salt;
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                HashMap<String, String> info_about_user = new HashMap<>(count);
                for (int i = 1; i <= count; i++) {
                    info_about_user.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                salted_password = info_about_user.get("password");
                salt = info_about_user.get("salt");
                String hashed_password = getSaltedPassword(password, salt);
                if (hashed_password.equals(salted_password)) {
                    user = new UserModel(info_about_user);
                    code = 1;
                } else {
                    code = -1;
                }
            } else {
                code = -2;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

    public int addService(String name, String price) throws SQLException {
        int code;
        String request = "INSERT INTO services(name, price) "
                + "VALUE" + " (" + "'" + name + "', '" + price + "')";
        databaseModel.addToTableRequest(request);
        code = 1;
        return code;
    }

    public int registerUser(String FIO, String login, String password) throws SQLException {
        int code;
        ResultSet result = findUserInDB(login);
        if (!result.next()) {//есть нет в ответе
            //TODO columnIndex to constant
            String salt = getSalt();
            String salted_password = getSaltedPassword(password, salt);
            String request = "INSERT INTO accounts(FIO, login, password, salt, role) VALUE ('" + FIO + "', '"
                    + login + "', '" + salted_password + "', '" + salt + "', 'Пользователь')";
            databaseModel.addToTableRequest(request);
            code = 1;
        } else {
            code = -1;
        }
        return code;
    }

    private String getSaltedPassword(String password, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(salt.getBytes());
        byte[] salted_password = md.digest(password.getBytes());
        return convertToHEX(salted_password);
    }

    private String getSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return convertToHEX(salt);
    }

    private String convertToHEX(byte[] hashed_password) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashed_password) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }

    private ResultSet findUserInDB(String login) {
        //TODO optimise request
        String request = "SELECT * FROM beauty.accounts WHERE accounts.login='" + login + "'";
        try {
            return databaseModel.request(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserRole() {
        return user.getRole();
    }

    public String getUserFIO() {
        return user.getFIO();
    }

    public ObservableList<ServiceModel> showSomeInfoServices() {
        String request = "SELECT * FROM beauty.services";
        ObservableList<ServiceModel> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        try {
            resultSet = databaseModel.request(request);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int column_count = metaData.getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> info_about_something = new HashMap<>(column_count);
                for (int i = 1; i <= column_count; i++) {
                    info_about_something.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                data.add(new ServiceModel(info_about_something));
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return data;
    }

    public ObservableList<ServiceModel> showSomeInfoOrders() {
        String request = "SELECT FIO, name, price FROM beauty.services a CROSS JOIN beauty.accounts b " +
                "RIGHT JOIN beauty.orders c ON c.service_id = a.id AND c.account_id = b.id";
        ObservableList<ServiceModel> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        try {
            resultSet = databaseModel.request(request);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int column_count = metaData.getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> info_about_user = new HashMap<>(column_count);
                for (int i = 1; i <= column_count; i++) {
                    info_about_user.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                data.add(new ServiceModel(info_about_user));
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return data;
    }

    public ObservableList<UserModel> showSomeInfoUsers() {
        String request = "SELECT * FROM beauty.accounts";
        ObservableList<UserModel> data = FXCollections.observableArrayList();
        ResultSet resultSet;
        try {
            resultSet = databaseModel.request(request);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int column_count = metaData.getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> info_about_user = new HashMap<>(column_count);
                for (int i = 1; i <= column_count; i++) {
                    info_about_user.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                data.add(new UserModel(info_about_user));
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return data;
    }

    public int orderService(String service_id) throws SQLException {
        int code;
        String request = "INSERT INTO orders(account_id, service_id)"
                + "VALUE ('" + user.getId() + "', '" + service_id + "')";
        databaseModel.addToTableRequest(request);
        code = 1;
        return code;
    }

    public void connectToDB() {
        databaseModel.createConnection();
    }

    public void disconnectDB() {
        try {
            databaseModel.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifySubscribers() {
        String request;
        ObservableList<UserModel> users = showSomeInfoUsers();
        for (UserModel user : users) {
            if (!user.getRole().equals("Администратор")) {
                request = "UPDATE accounts SET is_update='1' WHERE id=" + user.getId();
                databaseModel.addToTableRequest(request);
            }
        }
    }
}