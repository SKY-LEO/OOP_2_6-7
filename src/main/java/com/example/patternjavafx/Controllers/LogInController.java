package com.example.patternjavafx.Controllers;

import com.example.patternjavafx.Models.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class LogInController {

    private static final String ERROR_COLOR = "RED";

    MainModel mainModel;
    SceneController sceneController;

    @FXML
    private Label errorMessageLogIn;

    @FXML
    private Button logInButton;

    @FXML
    private TextField login;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Button passwordButton;

    @FXML
    private Label registration;

    @FXML
    void clearLogin(MouseEvent event) {
        login.clear();
        login.requestFocus();
        loginButton.setVisible(false);
    }

    @FXML
    void clearPassword(MouseEvent event) {
        password.clear();
        password.requestFocus();
        passwordButton.setVisible(false);
    }

    @FXML
    void setVisibilityLogin(KeyEvent event) {
        loginButton.setVisible(!login.getText().isEmpty());
    }

    @FXML
    void setVisibilityPassword(KeyEvent event) {
        passwordButton.setVisible(!password.getText().isEmpty());
    }

    @FXML
    void initialize() {
        loginButton.setVisible(false);
        passwordButton.setVisible(false);
    }

    @FXML
    public void userLogIn(ActionEvent event) throws IOException {
        if (!isEmptyFields()) {
            try {
                int code = mainModel.checkLoginPassword(login.getText(), password.getText());
                switch (code) {
                    case 1 -> {
                        System.out.println(mainModel.getUserRole() + ": " + mainModel.getUserFIO());
                        clearErrorMessage();
                        sceneController.activate(mainModel.getUserRole(), mainModel.getUserFIO());
                    }
                    case -1, -2 -> {
                        setErrorStyleTextField(login);
                        setErrorStyleTextField(password);
                        setTextError("Неправильный логин или пароль!");
                    }
                    default -> setTextError("Что-то пошло не так...");
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void openRegistration(MouseEvent event) throws IOException {
        sceneController.activate("Регистрация");
    }

    private boolean isEmptyFields() {
        boolean is_empty = true;
        cleanStyle();
        if (login.getText().isEmpty()) {
            setErrorStyleTextField(login);
            setTextError("Введите логин!");
        } else if (password.getText().isEmpty()) {
            setErrorStyleTextField(password);
            setTextError("Введите пароль!");
        } else {
            is_empty = false;
        }
        return is_empty;
    }

    private void setErrorStyleTextField(TextField textField) {
        textField.setStyle(textField.getStyle() + "-fx-border-color: " + ERROR_COLOR);
    }

    private void setTextError(String error_message) {
        errorMessageLogIn.setText(error_message);
    }

    private void cleanStyle() {
        login.setStyle("");
        password.setStyle("");
    }

    private void clearErrorMessage() {
        errorMessageLogIn.setText("");
    }

    public LogInController(MainModel mainModel, SceneController sceneController) {
        this.mainModel = mainModel;
        this.sceneController = sceneController;
    }
}


