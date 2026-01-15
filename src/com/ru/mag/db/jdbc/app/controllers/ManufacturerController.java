package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Manufacturer;
import com.ru.mag.db.jdbc.app.repository.ManufacturerRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManufacturerController {

    @FXML private TextField txtCompanyId;
    @FXML private TextField txtName;
    @FXML private TextField txtEik;
    @FXML private TextField txtPhone;

    @FXML private TableView<Manufacturer> tblManufacturers;
    @FXML private TableColumn<Manufacturer, Integer> colId;
    @FXML private TableColumn<Manufacturer, String> colName;
    @FXML private TableColumn<Manufacturer, String> colEik;
    @FXML private TableColumn<Manufacturer, String> colPhone;

    private final ManufacturerRepository repo = new ManufacturerRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCompanyId()).asObject());
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colEik.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEik()));
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPhone()));
        load();
    }

    private void load() {
        tblManufacturers.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        repo.create(new Manufacturer(
                Integer.parseInt(txtCompanyId.getText()),
                txtName.getText(),
                txtEik.getText(),
                txtPhone.getText()
        ));
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        Manufacturer m = tblManufacturers.getSelectionModel().getSelectedItem();
        if (m == null) return;
        txtCompanyId.setText(String.valueOf(m.getCompanyId()));
        txtCompanyId.setDisable(true);
        txtName.setText(m.getName());
        txtEik.setText(m.getEik());
        txtPhone.setText(m.getPhone());
    }

    @FXML
    private void update() {
        Manufacturer m = tblManufacturers.getSelectionModel().getSelectedItem();
        m.setName(txtName.getText());
        m.setEik(txtEik.getText());
        m.setPhone(txtPhone.getText());
        repo.update(m);
        load();
    }

    @FXML
    private void delete() {
        Manufacturer m = tblManufacturers.getSelectionModel().getSelectedItem();
        repo.delete(m.getCompanyId());
        clear();
        load();
    }

    private void clear() {
        txtCompanyId.clear();
        txtCompanyId.setDisable(false);
        txtName.clear();
        txtEik.clear();
        txtPhone.clear();
    }
}
