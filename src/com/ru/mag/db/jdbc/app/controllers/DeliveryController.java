package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Delivery;
import com.ru.mag.db.jdbc.app.repository.DeliveryRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DeliveryController {

    @FXML private TextField txtDeliveryId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtDocNo;
    @FXML private TextField txtPlate;
    @FXML private TextField txtCarrier;
    @FXML private TextField txtNote;
    @FXML private DatePicker dpDelivered;

    @FXML private TableView<Delivery> tblDeliveries;
    @FXML private TableColumn<Delivery, Integer> colId;
    @FXML private TableColumn<Delivery, Integer> colCustomer;
    @FXML private TableColumn<Delivery, String> colDoc;
    @FXML private TableColumn<Delivery, String> colDate;

    private final DeliveryRepository repo = new DeliveryRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getDeliveryId()).asObject());
        colCustomer.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getCustomerId()).asObject());
        colDoc.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDocNo()));
        colDate.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDeliveredAt().toString()));

        load();
    }

    private void load() {
        tblDeliveries.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        Delivery d = new Delivery();
        d.setDeliveryId(Integer.parseInt(txtDeliveryId.getText()));
        d.setCustomerId(Integer.parseInt(txtCustomerId.getText()));
        d.setDocNo(txtDocNo.getText());
        d.setDeliveredAt(dpDelivered.getValue());
        d.setVehiclePlate(txtPlate.getText());
        d.setCarrier(txtCarrier.getText());
        d.setNote(txtNote.getText());

        repo.create(d);
        clear();
        load();
    }

    @FXML
    private void update() {
        Delivery d = tblDeliveries.getSelectionModel().getSelectedItem();
        if (d == null) return;

        d.setCustomerId(Integer.parseInt(txtCustomerId.getText()));
        d.setDocNo(txtDocNo.getText());
        d.setDeliveredAt(dpDelivered.getValue());
        d.setVehiclePlate(txtPlate.getText());
        d.setCarrier(txtCarrier.getText());
        d.setNote(txtNote.getText());

        repo.update(d);
        load();
    }

    @FXML
    private void delete() {
        Delivery d = tblDeliveries.getSelectionModel().getSelectedItem();
        if (d == null) return;

        repo.delete(d.getDeliveryId());
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        Delivery d = tblDeliveries.getSelectionModel().getSelectedItem();
        if (d == null) return;

        txtDeliveryId.setText(String.valueOf(d.getDeliveryId()));
        txtDeliveryId.setDisable(true);
        txtCustomerId.setText(String.valueOf(d.getCustomerId()));
        txtDocNo.setText(d.getDocNo());
        dpDelivered.setValue(d.getDeliveredAt());
        txtPlate.setText(d.getVehiclePlate());
        txtCarrier.setText(d.getCarrier());
        txtNote.setText(d.getNote());
    }

    private void clear() {
        txtDeliveryId.clear();
        txtDeliveryId.setDisable(false);
        txtCustomerId.clear();
        txtDocNo.clear();
        dpDelivered.setValue(null);
        txtPlate.clear();
        txtCarrier.clear();
        txtNote.clear();
    }
}
