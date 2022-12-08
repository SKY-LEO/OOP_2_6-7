package com.example.patternjavafx.Controllers;

import com.example.patternjavafx.Models.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;


public class RegistrationController {

    private static final String ERROR_COLOR = "RED";

    private static final String INFORMATION_REGISTER = "Вы успешно зарегистрированы!";

    private static final String PASSWORD_REGEX = "([a-zA-Zа-яА-Я0-9_.%@&*!?-])" + "{6,20}$";

    private static final String LOGIN_REGEX = "^([a-zA-Zа-яА-Я0-9_.-])" + "{4,20}$";
    private static final String FIO_REGEX = "^([a-zA-Zа-яА-Я\\sёЁ])" + "{0,47}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern LOGIN_PATTERN = Pattern.compile(LOGIN_REGEX);
    private static final Pattern FIO_PATTERN = Pattern.compile(FIO_REGEX);

    MainModel mainModel;
    SceneController sceneController;

    @FXML
    private TextField FIO;

    @FXML
    private Label errorMessageRegister;

    @FXML
    private TextField login;

    @FXML
    private Button loginButton;

    @FXML
    private Button FIOButton;

    @FXML
    private Button passwordButton1;

    @FXML
    private Button passwordButton2;

    @FXML
    private Button goBackButton;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private Button sendRequest;

    @FXML
    void clearFIO(MouseEvent event) {
        FIO.clear();
        FIO.requestFocus();
        FIOButton.setVisible(false);
    }

    @FXML
    void clearLogin(MouseEvent event) {
        login.clear();
        login.requestFocus();
        loginButton.setVisible(false);
    }

    @FXML
    void clearPassword1(MouseEvent event) {
        password1.clear();
        password1.requestFocus();
        passwordButton1.setVisible(false);
    }

    @FXML
    void clearPassword2(MouseEvent event) {
        password2.clear();
        password2.requestFocus();
        passwordButton2.setVisible(false);
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        sceneController.goBack(this.getClass().getSimpleName());
    }

    @FXML
    void setVisibilityFIO(KeyEvent event) {
        FIOButton.setVisible(!FIO.getText().isEmpty());
    }

    @FXML
    void setVisibilityLogin(KeyEvent event) {
        loginButton.setVisible(!login.getText().isEmpty());
    }

    @FXML
    void setVisibilityPassword1(KeyEvent event) {
        passwordButton1.setVisible(!password1.getText().isEmpty());
    }

    @FXML
    void setVisibilityPassword2(KeyEvent event) {
        passwordButton2.setVisible(!password2.getText().isEmpty());
    }

    @FXML
    public void initialize() {
        FIOButton.setVisible(false);
        loginButton.setVisible(false);
        passwordButton1.setVisible(false);
        passwordButton2.setVisible(false);
    }

    @FXML
    void userRegister(ActionEvent event) throws IOException {
        ChainCheck chainCheck = ChainCheck.link(new EmptyCheck(), new RegexCheck(PASSWORD_PATTERN, FIO_PATTERN,
                LOGIN_PATTERN), new FullFIOCheck());
        int error_code = chainCheck.check(FIO.getText(), login.getText(), password1.getText(), password2.getText());
        if (error_code == 0) {
            try {
                int code = mainModel.registerUser(FIO.getText(), login.getText(), password1.getText());
                switch (code) {
                    case 1 -> {
                        clearErrorMessage();
                        showInformation(INFORMATION_REGISTER);
                        sceneController.activate("Авторизация");
                    }
                    case -1 -> {
                        setErrorStyleTextField(login);
                        setTextError("Такой логин уже существует!");
                    }
                    case -2 -> {
                        setErrorStyleTextField(FIO);
                        setTextError("Пользователь уже зарегистрирован!");
                    }
                    default -> setTextError("Что-то пошло не так...");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorDescription(error_code);
        }
    }

    private void errorDescription(int code) {
        clearStyle();
        switch (code) {
            case -1 -> {
                setErrorStyleTextField(FIO);
                setTextError("Введите ФИО!");
            }
            case -2 -> {
                setErrorStyleTextField(login);
                setTextError("Введите логин!");
            }
            case -3 -> {
                setErrorStyleTextField(password1);
                setTextError("Введите пароль!");
            }
            case -4 -> {
                setErrorStyleTextField(password2);
                setTextError("Введите пароль повторно!");
            }
            case -5 -> {
                setErrorStyleTextField(password1);
                setErrorStyleTextField(password2);
                setTextError("Пароли не совпадают!");
            }
            case -10 -> {
                setErrorStyleTextField(FIO);
                setTextError("ФИО должно состоять из букв и пробелов!");
            }
            case -11 -> {
                setErrorStyleTextField(login);
                setTextError("Логин состоит минимум из 4 букв, цифр, \"-\", \"_\", \".\"!");
            }
            case -12 -> {
                setErrorStyleTextField(password1);
                setTextError("Пароль минимум из 6 букв, цифр и спецсимволов!");
            }
            case -13 -> {
                setErrorStyleTextField(password2);
                setTextError("Пароль минимум из 6 букв, цифр и спецсимволов!");
            }
            case -20 -> {
                setErrorStyleTextField(FIO);
                setTextError("Напишите ФИО полностью!");
            }
        }
    }

    private void setErrorStyleTextField(TextField textField) {
        textField.setStyle(textField.getStyle() + "-fx-border-color: " + ERROR_COLOR);
    }

    private void setTextError(String error_message) {
        errorMessageRegister.setText(error_message);
    }

    private void clearStyle() {
        FIO.setStyle("");
        login.setStyle("");
        password1.setStyle("");
        password2.setStyle("");
    }

    private void clearErrorMessage() {
        errorMessageRegister.setText("");
    }

    private void showInformation(String message) {
        ButtonType ok_button = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ok_button);
        alert.setTitle("Информация");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public RegistrationController(MainModel mainModel, SceneController sceneController) {
        this.mainModel = mainModel;
        this.sceneController = sceneController;
    }
}

