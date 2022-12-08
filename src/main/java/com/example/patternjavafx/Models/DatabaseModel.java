package com.example.patternjavafx.Models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;


public class DatabaseModel {

    DatabaseSettings databaseSettings = new DatabaseSettings();
    Connection connection = null;
    Statement statement = null;

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void createConnection(){
        try {
            connection = DriverManager.getConnection(databaseSettings.getUrl(), databaseSettings.getUsername(),
                    databaseSettings.getPassword());
            statement = connection.createStatement();
            System.out.println("Подключение к базе данных установлено");
        } catch (Exception e) {
            showAlert(e);
        }
    }

    void disconnect() throws SQLException {
        try {
            if (connection != null) {
                if (!connection.isClosed() || !statement.isClosed()) {
                    connection.close();
                    statement.close();
                    System.out.println("Отключено от базы данных");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public ResultSet request(String request) throws SQLException {
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(request);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return resultSet;

    }

    void addToTableRequest(String request) {
        try {
            statement.executeUpdate(request);
        } catch (SQLException e) {
            showAlert(e);
        }
    }

    DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    void showAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Request Error");
        alert.setHeaderText("Request Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }
}
