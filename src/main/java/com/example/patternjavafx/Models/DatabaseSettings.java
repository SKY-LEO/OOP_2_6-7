package com.example.patternjavafx.Models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class DatabaseSettings {
    private final String url;
    private final String username;
    private final String password;
    private final String file_name = "database.properties";

    public DatabaseSettings() {
        Properties properties = new Properties();
        try {
            InputStream in = Files.newInputStream(Paths.get(file_name));
            properties.load(in);
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
