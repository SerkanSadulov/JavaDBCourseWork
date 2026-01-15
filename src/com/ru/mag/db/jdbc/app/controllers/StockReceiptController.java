package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.StockReceipt;
import com.ru.mag.db.jdbc.app.repository.StockReceiptRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StockReceiptController {

    @FXML private TextField txtReceiptId;
    @FXML private TextField txtSupplierId;
    @FXML private TextField txtDocNo;
    @FXML private TextField txtNote;
    @FXML private DatePicker dpReceived;

    @FXML private TableView<StockReceipt> tblReceipts;
    @FXML private TableColumn<StockReceipt, Integer> colId;
    @FXML private TableColumn<StockReceipt, Integer> colSupplier;
    @FXML private TableColumn<StockReceipt, String> colDoc;
    @FXML private TableColumn<StockReceipt, String> colDate;

    private final StockReceiptRepository repo = new StockReceiptRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getReceiptId()).asObject());
        colSupplier.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getSupplierId()).asObject());
        colDoc.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDocNo()));
        colDate.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getReceivedAt().toString()));

        load();
    }

    private void load() {
        tblReceipts.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        StockReceipt r = new StockReceipt();
        r.setReceiptId(Integer.parseInt(txtReceiptId.getText()));
        r.setSupplierId(Integer.parseInt(txtSupplierId.getText()));
        r.setDocNo(txtDocNo.getText());
        r.setReceivedAt(dpReceived.getValue());
        r.setNote(txtNote.getText());

        repo.create(r);
        clear();
        load();
    }

    @FXML
    private void update() {
        StockReceipt r = tblReceipts.getSelectionModel().getSelectedItem();
        if (r == null) return;

        r.setSupplierId(Integer.parseInt(txtSupplierId.getText()));
        r.setDocNo(txtDocNo.getText());
        r.setReceivedAt(dpReceived.getValue());
        r.setNote(txtNote.getText());

        repo.update(r);
        load();
    }

    @FXML
    private void delete() {
        StockReceipt r = tblReceipts.getSelectionModel().getSelectedItem();
        if (r == null) return;

        repo.delete(r.getReceiptId());
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        StockReceipt r = tblReceipts.getSelectionModel().getSelectedItem();
        if (r == null) return;

        txtReceiptId.setText(String.valueOf(r.getReceiptId()));
        txtReceiptId.setDisable(true);
        txtSupplierId.setText(String.valueOf(r.getSupplierId()));
        txtDocNo.setText(r.getDocNo());
        dpReceived.setValue(r.getReceivedAt());
        txtNote.setText(r.getNote());
    }

    private void clear() {
        txtReceiptId.clear();
        txtReceiptId.setDisable(false);
        txtSupplierId.clear();
        txtDocNo.clear();
        dpReceived.setValue(null);
        txtNote.clear();
    }
}
