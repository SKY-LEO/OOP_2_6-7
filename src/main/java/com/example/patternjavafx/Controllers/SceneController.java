package com.example.patternjavafx.Controllers;

import com.example.patternjavafx.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneController {
    private HashMap<String, ControllerAndPane> sceneMap = new HashMap<>();
    private Stage main_stage;

    public SceneController(Stage main_stage) {
        this.main_stage = main_stage;
    }

    public void addScreen(String name, String pane, Object controller, int width, int height) {
        sceneMap.put(name, new ControllerAndPane(controller, pane, width, height));
    }


    public void removeScreen(String name) {
        sceneMap.remove(name);
    }

    public void activate(String name, String FIO) throws IOException {
        main_stage.setTitle(name + ": " + FIO);
        setSceneSettings(name);
    }

    public void activate(String name) throws IOException {
        main_stage.setTitle(name);
        setSceneSettings(name);
    }

    public void goBack(String fxmFile) throws IOException {
        //if (fxmFile.equals("RegistrationController") || fxmFile.equals("ResetPasswordController") ||
        //        fxmFile.equals("HRManagerController") || fxmFile.equals("AdministratorController")) {
            activate("Авторизация");
        //}
    }

    private void setSceneSettings(String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(sceneMap.get(name).getPane()));
        fxmlLoader.setController(sceneMap.get(name).getController());
        main_stage.setWidth(sceneMap.get(name).getWidth());
        main_stage.setHeight(sceneMap.get(name).getHeight());
        main_stage.getScene().setRoot(fxmlLoader.load());
    }
}
