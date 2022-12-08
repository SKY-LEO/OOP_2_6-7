package com.example.patternjavafx;

import com.example.patternjavafx.Controllers.*;
import com.example.patternjavafx.Models.MainModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Application extends javafx.application.Application {
    MainModel mainModel;
    @Override
    public void start(Stage primary_stage) throws IOException {
        mainModel = new MainModel();
        mainModel.connectToDB();
        SceneController sceneController = new SceneController(primary_stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        fxmlLoader.setController(new LogInController(mainModel, sceneController));
        Scene scene = new Scene(fxmlLoader.load());
        primary_stage.setScene(scene);
        primary_stage.setResizable(false);
        primary_stage.show();
        addScenes(sceneController, mainModel);
        sceneController.activate("Авторизация");
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        mainModel.disconnectDB();
    }

    public void addScenes(SceneController sceneController, MainModel mainModel) {
        sceneController.addScreen("Авторизация", "logIn.fxml",
                new LogInController(mainModel, sceneController), 555, 416);
        sceneController.addScreen("Регистрация", "registration.fxml",
                new RegistrationController(mainModel, sceneController), 555, 416);
        sceneController.addScreen("Администратор", "administrator.fxml",
                new AdministratorController(mainModel, sceneController), 795, 558);
        sceneController.addScreen("Пользователь", "user.fxml",
                new UserController(mainModel, sceneController), 795, 558);
    }
}