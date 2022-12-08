package com.example.patternjavafx.Controllers;

import com.example.patternjavafx.Models.MainModel;
import com.example.patternjavafx.Models.ServiceModel;
import com.example.patternjavafx.Models.UserModel;
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

public class AdministratorController {

    private static final String ERROR_COLOR = "RED";
    private static final String CONFIRMATION_LOG_OUT = "Вы действительно хотите выйти из аккаунта?";

    private static final String CONFIRMATION_ADD = "Вы действительно хотите добавить новую услугу?";

    private static final String INFORMATION_ADD = "Услуга успешно добавлена!";


    MainModel mainModel;
    SceneController sceneController;


    @FXML
    private TableColumn<ServiceModel, String> FIO_user;

    @FXML
    private Button addServiceButton;

    @FXML
    private Tab addServiceTab;

    @FXML
    private Button clearRequestButton;

    @FXML
    private Label errorMessageAddService;

    @FXML
    private Label errorMessageDeleteRecord;

    @FXML
    private Button goBackButton;

    @FXML
    private TableView<ServiceModel> order_table;

    @FXML
    private Tab ordersTab;

    @FXML
    private Button serviceButton;

    @FXML
    private Button priceButton;

    @FXML
    private TextField price;

    @FXML
    private TextField search;

    @FXML
    private TextField service;

    @FXML
    private TableColumn<ServiceModel, String> service_name;

    @FXML
    private TableColumn<ServiceModel, String> service_name_add;

    @FXML
    private TableColumn<ServiceModel, String> service_price;

    @FXML
    private TableColumn<ServiceModel, String> service_price_add;

    @FXML
    private TableView<ServiceModel> services_table;

    @FXML
    private TabPane tabs;

    @FXML
    private Tab allUsersTab;

    @FXML
    private TableView<UserModel> users_table;

    @FXML
    private TableColumn<UserModel, String> FIO_users;

    @FXML
    private TableColumn<UserModel, String> ID_users;

    @FXML
    private TableColumn<UserModel, String> login_users;

    @FXML
    private TableColumn<UserModel, String> password_users;

    @FXML
    private TableColumn<UserModel, String> salt_users;

    @FXML
    private TableColumn<UserModel, String> role_users;

    @FXML
    private TableColumn<UserModel, String> is_update_users;

    @FXML
    void addService(ActionEvent event) {
        clearStyle();
        if (!isEmptyFields()) {
            try {
                clearErrorMessage(errorMessageAddService);
                if (showConfirmation(CONFIRMATION_ADD +
                        "\nНазвание услуги: " + service.getText() +
                        "\nСтоимость услуги: " + price.getText())) {
                    int code = mainModel.addService(service.getText(),
                            price.getText());
                    if (code == 1) {
                        showInformation(INFORMATION_ADD);
                        updateTables();
                        mainModel.notifySubscribers();
                    } else {
                        setTextError(errorMessageAddService, "Что-то пошло не так...");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    void clearService(MouseEvent event) {
        service.clear();
        service.requestFocus();
        serviceButton.setVisible(false);
    }

    @FXML
    void clearPrice(MouseEvent event) {
        price.clear();
        price.requestFocus();
        priceButton.setVisible(false);
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
    void setVisibilityPrice(KeyEvent event) {
        priceButton.setVisible(!price.getText().isEmpty());
    }

    @FXML
    void setVisibilityService(KeyEvent event) {
        serviceButton.setVisible(!service.getText().isEmpty());
    }

    @FXML
    void setVisibilitySearch(KeyEvent event) {
        clearRequestButton.setVisible(!search.getText().isEmpty());
    }

    @FXML
    public void initialize() {
        priceButton.setVisible(false);
        serviceButton.setVisible(false);
        clearRequestButton.setVisible(false);
        ObservableList<ServiceModel> users = mainModel.showSomeInfoOrders();
        ObservableList<ServiceModel> services = mainModel.showSomeInfoServices();
        ObservableList<UserModel> all_users = mainModel.showSomeInfoUsers();
        FilteredList<ServiceModel> filteredList_users = new FilteredList<>(users, b -> true);
        FilteredList<ServiceModel> filteredList_calls = new FilteredList<>(services, b -> true);
        FilteredList<UserModel> filteredList_all_users = new FilteredList<>(all_users, b -> true);
        fillData(users, FIO_user, service_name, service_price, order_table);
        fillData(services, service_name_add, service_price_add, services_table);
        fillData(all_users, ID_users, FIO_users, login_users, password_users, salt_users, role_users, is_update_users,
                users_table);
        search.textProperty()
                .addListener((observable, oldValue, newValue) -> searchSomething(filteredList_calls,
                        filteredList_users, filteredList_all_users, oldValue, newValue));
        tabs.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> tabsChanged());
    }

    private void tabsChanged() {
        search.clear();
        updateTables();
    }

    private void fillData(ObservableList<ServiceModel> users, TableColumn<ServiceModel, String> FIO,
                          TableColumn<ServiceModel, String> service_name, TableColumn<ServiceModel, String> service_price,
                          TableView<ServiceModel> users_table) {
        FIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        service_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        service_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        users_table.setItems(users);
    }

    private void fillData(ObservableList<ServiceModel> services,
                          TableColumn<ServiceModel, String> service_name,
                          TableColumn<ServiceModel, String> service_price,
                          TableView<ServiceModel> services_table) {
        service_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        service_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        services_table.setItems(services);
    }

    private void fillData(ObservableList<UserModel> users,
                          TableColumn<UserModel, String> id,
                          TableColumn<UserModel, String> fio,
                          TableColumn<UserModel, String> login,
                          TableColumn<UserModel, String> password,
                          TableColumn<UserModel, String> salt,
                          TableColumn<UserModel, String> role,
                          TableColumn<UserModel, String> is_update,
                          TableView<UserModel> users_table) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        fio.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        login.setCellValueFactory(new PropertyValueFactory<>("login"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        salt.setCellValueFactory(new PropertyValueFactory<>("salt"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        is_update.setCellValueFactory(new PropertyValueFactory<>("is_update"));
        users_table.setItems(users);
    }

    private void updateTables() {
        //services_table.getItems().clear();
        services_table.setItems(mainModel.showSomeInfoServices());
        //services_table.getItems().setAll(mainModel.showSomeInfoServices());
        //order_table.getItems().clear();
        order_table.setItems(mainModel.showSomeInfoOrders());
        //order_table.getItems().setAll(mainModel.showSomeInfoOrders());
        //users_table.getItems().clear();
        users_table.setItems(mainModel.showSomeInfoUsers());
        //users_table.getItems().setAll(mainModel.showSomeInfoUsers());
    }

    private boolean isEmptyFields() {
        boolean is_empty = true;
        clearStyle();
        if (service.getText().isEmpty()) {
            setErrorTextField(service);
            setTextError(errorMessageAddService, "Напишите название услуги!");
        } else if (price.getText().isEmpty()) {
            setErrorTextField(price);
            setTextError(errorMessageAddService, "Напишите стоимость услуги!");
        } else {
            is_empty = false;
        }
        return is_empty;
    }

    private void setErrorTextField(TextField textField) {
        textField.setStyle(textField.getStyle() + "-fx-border-color: " + ERROR_COLOR);
    }

    private void clearStyle() {
        service.setStyle("");
        price.setStyle("");
    }

    private void searchSomething(FilteredList<ServiceModel> filteredList_services,
                                 FilteredList<ServiceModel> filteredList_users,
                                 FilteredList<UserModel> filteredList_all_users,
                                 String oldValue, String newValue) {
        Tab selected_tab = tabs.getSelectionModel().getSelectedItem();
        if (selected_tab == ordersTab) {
            filteredList_users.setPredicate(user -> {
                if (newValue == null || oldValue == null) {
                    return true;
                }
                String searchableData = newValue.toLowerCase();
                return user.getFIO().toLowerCase().contains(searchableData)
                        || user.getPrice().toLowerCase().contains(searchableData)
                        || user.getName().toLowerCase().contains(searchableData);
            });
            SortedList<ServiceModel> sortedList = new SortedList<>(filteredList_users);
            sortedList.comparatorProperty().bind(order_table.comparatorProperty());
            order_table.setItems(sortedList);
        } else if (selected_tab == addServiceTab) {
            filteredList_services.setPredicate(service -> {
                if (newValue == null || oldValue == null) {
                    return true;
                }
                String searchableData = newValue.toLowerCase();
                return service.getName().toLowerCase().contains(searchableData)
                        || service.getPrice().toLowerCase().contains(searchableData);
            });
            SortedList<ServiceModel> sortedList = new SortedList<>(filteredList_services);
            sortedList.comparatorProperty().bind(services_table.comparatorProperty());
            services_table.setItems(sortedList);
        } else if (selected_tab == allUsersTab) {
            filteredList_all_users.setPredicate(all_users -> {
                if (newValue == null || oldValue == null) {
                    return true;
                }
                String searchableData = newValue.toLowerCase();
                return all_users.getId().toLowerCase().contains(searchableData)
                        || all_users.getFIO().toLowerCase().contains(searchableData)
                        || all_users.getLogin().toLowerCase().contains(searchableData)
                        || all_users.getRole().toLowerCase().contains(searchableData);
            });
            SortedList<UserModel> sortedList = new SortedList<>(filteredList_all_users);
            sortedList.comparatorProperty().bind(users_table.comparatorProperty());
            users_table.setItems(sortedList);
        }
    }

    private void setTextError(Label errorMessage, String error_message) {
        errorMessage.setText(error_message);
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

    public AdministratorController(MainModel mainModel, SceneController sceneController) {
        this.mainModel = mainModel;
        this.sceneController = sceneController;
    }
}
