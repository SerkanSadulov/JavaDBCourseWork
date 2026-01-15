package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.DeliveryProduct;
import com.ru.mag.db.jdbc.app.repository.DeliveryProductRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DeliveryProductController {

    @FXML private TextField txtDeliveryProductId;
    @FXML private TextField txtDeliveryId;
    @FXML private TextField txtProductId;
    @FXML private TextField txtBatchId;
    @FXML private TextField txtQty;
    @FXML private TextField txtUnitPrice;

    @FXML private TableView<DeliveryProduct> tblLines;
    @FXML private TableColumn<DeliveryProduct, Integer> colId;
    @FXML private TableColumn<DeliveryProduct, Integer> colDelivery;
    @FXML private TableColumn<DeliveryProduct, Integer> colProduct;
    @FXML private TableColumn<DeliveryProduct, Double> colQty;

    private final DeliveryProductRepository repo = new DeliveryProductRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getDeliveryProductId()).asObject());
        colDelivery.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getDeliveryId()).asObject());
        colProduct.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getProductId()).asObject());
        colQty.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getQty()).asObject());

        load();
    }

    private void load() {
        tblLines.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        DeliveryProduct dp = new DeliveryProduct();
        dp.setDeliveryProductId(Integer.parseInt(txtDeliveryProductId.getText()));
        dp.setDeliveryId(Integer.parseInt(txtDeliveryId.getText()));
        dp.setProductId(Integer.parseInt(txtProductId.getText()));
        dp.setBatchId(Integer.parseInt(txtBatchId.getText()));
        dp.setQty(Double.parseDouble(txtQty.getText()));
        dp.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));

        repo.create(dp);
        clear();
        load();
    }

    @FXML
    private void update() {
        DeliveryProduct dp = tblLines.getSelectionModel().getSelectedItem();
        if (dp == null) return;

        dp.setDeliveryId(Integer.parseInt(txtDeliveryId.getText()));
        dp.setProductId(Integer.parseInt(txtProductId.getText()));
        dp.setBatchId(Integer.parseInt(txtBatchId.getText()));
        dp.setQty(Double.parseDouble(txtQty.getText()));
        dp.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));

        repo.update(dp);
        load();
    }

    @FXML
    private void delete() {
        DeliveryProduct dp = tblLines.getSelectionModel().getSelectedItem();
        if (dp == null) return;

        repo.delete(dp.getDeliveryProductId());
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        DeliveryProduct dp = tblLines.getSelectionModel().getSelectedItem();
        if (dp == null) return;

        txtDeliveryProductId.setText(String.valueOf(dp.getDeliveryProductId()));
        txtDeliveryProductId.setDisable(true);
        txtDeliveryId.setText(String.valueOf(dp.getDeliveryId()));
        txtProductId.setText(String.valueOf(dp.getProductId()));
        txtBatchId.setText(String.valueOf(dp.getBatchId()));
        txtQty.setText(String.valueOf(dp.getQty()));
        txtUnitPrice.setText(String.valueOf(dp.getUnitPrice()));
    }

    private void clear() {
        txtDeliveryProductId.clear();
        txtDeliveryProductId.setDisable(false);
        txtDeliveryId.clear();
        txtProductId.clear();
        txtBatchId.clear();
        txtQty.clear();
        txtUnitPrice.clear();
    }
}
