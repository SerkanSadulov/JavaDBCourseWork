package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Batch;
import com.ru.mag.db.jdbc.app.repository.BatchRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BatchController {

    @FXML private TextField txtBatchId;
    @FXML private TextField txtProductId;
    @FXML private TextField txtBatchNo;
    @FXML private DatePicker dpExpiry;

    @FXML private TableView<Batch> tblBatches;
    @FXML private TableColumn<Batch, Integer> colId;
    @FXML private TableColumn<Batch, Integer> colProduct;
    @FXML private TableColumn<Batch, String> colBatchNo;
    @FXML private TableColumn<Batch, String> colExpiry;

    private final BatchRepository repo = new BatchRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getBatchId()).asObject());
        colProduct.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getProductId()).asObject());
        colBatchNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBatchNo()));
        colExpiry.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getExpiryDate().toString()));

        load();
    }

    private void load() {
        tblBatches.getItems().setAll(repo.findAll());
    }

    @FXML
    void create() {
        Batch b = new Batch();
        b.setBatchId(Integer.parseInt(txtBatchId.getText()));
        b.setProductId(Integer.parseInt(txtProductId.getText()));
        b.setBatchNo(txtBatchNo.getText());
        b.setExpiryDate(dpExpiry.getValue());

        repo.create(b);
        clear();
        load();
    }

    @FXML
    void update() {
        Batch b = tblBatches.getSelectionModel().getSelectedItem();
        if (b == null) return;

        b.setProductId(Integer.parseInt(txtProductId.getText()));
        b.setBatchNo(txtBatchNo.getText());
        b.setExpiryDate(dpExpiry.getValue());

        repo.update(b);
        load();
    }

    @FXML
    void delete() {
        Batch b = tblBatches.getSelectionModel().getSelectedItem();
        if (b == null) return;

        repo.delete(b.getBatchId());
        clear();
        load();
    }

    @FXML
    void onSelect() {
        Batch b = tblBatches.getSelectionModel().getSelectedItem();
        if (b == null) return;

        txtBatchId.setText(String.valueOf(b.getBatchId()));
        txtBatchId.setDisable(true);
        txtProductId.setText(String.valueOf(b.getProductId()));
        txtBatchNo.setText(b.getBatchNo());
        dpExpiry.setValue(b.getExpiryDate());
    }

    private void clear() {
        txtBatchId.clear();
        txtBatchId.setDisable(false);
        txtProductId.clear();
        txtBatchNo.clear();
        dpExpiry.setValue(null);
    }
}
