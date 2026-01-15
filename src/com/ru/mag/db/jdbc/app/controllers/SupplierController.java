package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Supplier;
import com.ru.mag.db.jdbc.app.repository.SupplierRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SupplierController {

    @FXML private TextField txtCompanyId;
    @FXML private TextField txtName;
    @FXML private TextField txtEik;
    @FXML private TextField txtPhone;

    @FXML private TableView<Supplier> tblSuppliers;
    @FXML private TableColumn<Supplier, Integer> colId;
    @FXML private TableColumn<Supplier, String> colName;
    @FXML private TableColumn<Supplier, String> colEik;
    @FXML private TableColumn<Supplier, String> colPhone;

    private final SupplierRepository repo = new SupplierRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompanyId()).asObject());
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colEik.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEik()));
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPhone()));
        load();
    }

    private void load() {
        tblSuppliers.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        repo.create(new Supplier(
                Integer.parseInt(txtCompanyId.getText()),
                txtName.getText(),
                txtEik.getText(),
                txtPhone.getText()
        ));
        load();
    }

    @FXML
    private void onSelect() {
        Supplier s = tblSuppliers.getSelectionModel().getSelectedItem();
        if (s == null) return;
        txtCompanyId.setText(String.valueOf(s.getCompanyId()));
        txtCompanyId.setDisable(true);
        txtName.setText(s.getName());
        txtEik.setText(s.getEik());
        txtPhone.setText(s.getPhone());
    }

    @FXML
    private void update() {
        Supplier s = tblSuppliers.getSelectionModel().getSelectedItem();
        s.setName(txtName.getText());
        s.setEik(txtEik.getText());
        s.setPhone(txtPhone.getText());
        repo.update(s);
        load();
    }

    @FXML
    private void delete() {
        Supplier s = tblSuppliers.getSelectionModel().getSelectedItem();
        repo.delete(s.getCompanyId());
        load();
    }
}
