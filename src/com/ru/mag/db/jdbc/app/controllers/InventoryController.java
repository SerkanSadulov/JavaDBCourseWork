package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Inventory;
import com.ru.mag.db.jdbc.app.repository.InventoryRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InventoryController {

    @FXML private TextField txtBatchId;
    @FXML private TextField txtQty;
    @FXML private TextField txtMinQty;

    @FXML private TableView<Inventory> tblInventory;
    @FXML private TableColumn<Inventory, Integer> colBatch;
    @FXML private TableColumn<Inventory, Double> colQty;
    @FXML private TableColumn<Inventory, Double> colMin;

    private final InventoryRepository repo = new InventoryRepository();

    @FXML
    public void initialize() {
        colBatch.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getBatchId()).asObject());
        colQty.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getQty()).asObject());
        colMin.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getMinQty()).asObject());

        load();
    }

    private void load() {
        tblInventory.getItems().setAll(repo.findAll());
    }

    @FXML
    private void save() {
        Inventory i = new Inventory();
        i.setBatchId(Integer.parseInt(txtBatchId.getText()));
        i.setQty(Double.parseDouble(txtQty.getText()));
        i.setMinQty(Double.parseDouble(txtMinQty.getText()));

        repo.upsert(i);
        clear();
        load();
    }

    @FXML
    private void delete() {
        Inventory i = tblInventory.getSelectionModel().getSelectedItem();
        if (i == null) return;

        repo.delete(i.getBatchId());
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        Inventory i = tblInventory.getSelectionModel().getSelectedItem();
        if (i == null) return;

        txtBatchId.setText(String.valueOf(i.getBatchId()));
        txtBatchId.setDisable(true);
        txtQty.setText(String.valueOf(i.getQty()));
        txtMinQty.setText(String.valueOf(i.getMinQty()));
    }

    private void clear() {
        txtBatchId.clear();
        txtBatchId.setDisable(false);
        txtQty.clear();
        txtMinQty.clear();
    }
}
