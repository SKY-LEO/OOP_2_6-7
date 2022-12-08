package com.example.patternjavafx.Controllers;

import com.example.patternjavafx.Models.MainModel;
import com.example.patternjavafx.Models.ServiceModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UserController {
    private static final String ERROR_COLOR = "RED";

    private static final String CONFIRMATION_ORDER = "Вы действительно хотите заказать услугу?";


    private static final String CONFIRMATION_LOG_OUT = "Вы действительно хотите выйти из аккаунта?";

    private static final String INFORMATION_ADD = "Услуга успешно заказана!";

    MainModel mainModel;
    SceneController sceneController;

    private String service_id;

    @FXML
    private TextField FIO;

    @FXML
    private Button clearRequestButton;

    @FXML
    private Label errorMessageOrderService;

    @FXML
    private Button goBackButton;

    @FXML
    private TextField service;

    @FXML
    private TextField price;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<ServiceModel, String> service_name;

    @FXML
    private TableColumn<ServiceModel, String> service_price;

    @FXML
    private TableView<ServiceModel> services_table;

    @FXML
    void orderService(ActionEvent event) {
        clearStyle(FIO, service, price);
        if (!isEmptyFields(errorMessageOrderService, FIO, service, price)) {
            try {
                clearErrorMessage(errorMessageOrderService);
                if (showConfirmation(CONFIRMATION_ORDER +
                        "\nФИО: " + FIO.getText() +
                        "\nНазвание услуги: " + service.getText() +
                        "\nСтоимость услуги: " + price.getText())) {
                    int code = mainModel.orderService(service_id);
                    if (code == 1) {
                        showInformation(INFORMATION_ADD);
                        updateTables();
                    } else {
                        setTextError(errorMessageOrderService, "Что-то пошло не так...");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void setVisibilitySearch(KeyEvent event) {
        clearRequestButton.setVisible(!search.getText().isEmpty());
    }

    @FXML
    void clearRequest(MouseEvent event) {
        search.clear();
        search.requestFocus();
        clearRequestButton.setVisible(false);
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        if (showConfirmation(CONFIRMATION_LOG_OUT)) {
            sceneController.goBack(this.getClass().getSimpleName());
        }
    }

    @FXML
    public void initialize() {
        clearRequestButton.setVisible(false);
        disableItems();
        ObservableList<ServiceModel> services = mainModel.showSomeInfoServices();
        FilteredList<ServiceModel> filteredList = new FilteredList<>(services, b -> true);
        fillData(services, service_name, service_price, services_table);
        services_table.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> fillDataAboutService(newValue));
        search.textProperty()
                .addListener((observable, oldValue, newValue) -> searchSomething(filteredList, oldValue, newValue));
    }

    private void fillDataAboutService(ServiceModel serviceModel) {
        if (serviceModel != null) {
            enableItems();
            clearSelectedItems();
            service_id = serviceModel.getId();
            FIO.setText(mainModel.getUserFIO());
            service.setText(serviceModel.getName());
            price.setText(serviceModel.getPrice());
        }
    }

    private void searchSomething(FilteredList<ServiceModel> filteredList, String oldValue, String newValue) {
        filteredList.setPredicate(service -> {
            if (newValue == null || oldValue == null) {
                return true;
            }
            String searchableData = newValue.toLowerCase();
            return service.getName().toLowerCase().contains(searchableData)
                    || service.getPrice().toLowerCase().contains(searchableData);
        });
        SortedList<ServiceModel> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(services_table.comparatorProperty());
        services_table.setItems(sortedList);
    }

    private void clearSelectedItems() {
        service.clear();
        price.clear();
    }

    private void disableItems() {
        FIO.setDisable(true);
        service.setDisable(true);
        price.setDisable(true);
    }

    private void enableItems() {
        FIO.setDisable(false);
        service.setDisable(false);
        price.setDisable(false);
    }

    private void fillData(ObservableList<ServiceModel> services,
                          TableColumn<ServiceModel, String> service_name,
                          TableColumn<ServiceModel, String> service_price,
                          TableView<ServiceModel> services_table) {
        service_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        service_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        services_table.setItems(services);
    }

    private boolean isEmptyFields(Label errorMessage, TextField FIO, TextField service_name, TextField service_price) {
        boolean is_empty = true;
        if (FIO.getText().isEmpty()) {
            setErrorStyleTextField(FIO);
            setTextError(errorMessage, "Введите ФИО!");
        } else if (service_name.getText().isEmpty()) {
            setErrorStyleTextField(service_name);
            setTextError(errorMessage, "Выберите услугу!");
        } else if (service_price.getText().isEmpty()) {
            setErrorStyleTextField(service_price);
            setTextError(errorMessage, "Выберите услугу!");
        } else {
            is_empty = false;
        }
        return is_empty;
    }

    private void updateTables() {
        services_table.setItems(mainModel.showSomeInfoServices());
    }

    private void setErrorStyleTextField(TextField textField) {
        textField.setStyle(textField.getStyle() + "-fx-border-color: " + ERROR_COLOR);
    }

    private void setTextError(Label errorMessage, String error_message) {
        errorMessage.setText(error_message);
    }

    private void clearStyle(TextField FIO, TextField service_name, TextField service_price) {
        FIO.setStyle("");
        service_name.setStyle("");
        service_price.setStyle("");
    }

    private void clearErrorMessage(Label errorMessage) {
        errorMessage.setText("");
    }

    private boolean showConfirmation(String message) {
        boolean is_confirmed = false;
        ButtonType ok_button = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel_button = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ok_button, cancel_button);
        alert.setTitle("Подтвердите действие");
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok_button) {
            is_confirmed = true;
        }
        return is_confirmed;
    }

    private void showInformation(String message) {
        ButtonType ok_button = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ok_button);
        alert.setTitle("Информация");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public UserController(MainModel mainModel, SceneController sceneController) {
        this.mainModel = mainModel;
        this.sceneController = sceneController;
    }
}

