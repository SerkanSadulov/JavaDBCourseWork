package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Customer;
import com.ru.mag.db.jdbc.app.repository.CustomerRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerController {

    @FXML private TextField txtCompanyId;
    @FXML private TextField txtName;
    @FXML private TextField txtEik;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPharmacyCode;

    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEik;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colPharmacyCode;

    private final CustomerRepository repo = new CustomerRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompanyId()).asObject());
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colEik.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEik()));
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPhone()));
        colPharmacyCode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPharmacyCode()));
        load();
    }

    private void load() {
        tblCustomers.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        repo.create(new Customer(
                Integer.parseInt(txtCompanyId.getText()),
                txtName.getText(),
                txtEik.getText(),
                txtPhone.getText(),
                txtPharmacyCode.getText()
        ));
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        Customer c = tblCustomers.getSelectionModel().getSelectedItem();
        if (c == null) return;
        txtCompanyId.setText(String.valueOf(c.getCompanyId()));
        txtCompanyId.setDisable(true);
        txtName.setText(c.getName());
        txtEik.setText(c.getEik());
        txtPhone.setText(c.getPhone());
        txtPharmacyCode.setText(c.getPharmacyCode());
    }

    @FXML
    private void update() {
        Customer c = tblCustomers.getSelectionModel().getSelectedItem();
        c.setName(txtName.getText());
        c.setEik(txtEik.getText());
        c.setPhone(txtPhone.getText());
        c.setPharmacyCode(txtPharmacyCode.getText());
        repo.update(c);
        load();
    }

    @FXML
    private void delete() {
        Customer c = tblCustomers.getSelectionModel().getSelectedItem();
        repo.delete(c.getCompanyId());
        clear();
        load();
    }

    private void clear() {
        txtCompanyId.clear();
        txtCompanyId.setDisable(false);
        txtName.clear();
        txtEik.clear();
        txtPhone.clear();
        txtPharmacyCode.clear();
    }
}
