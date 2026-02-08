package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.DeliveryReportRow;
import com.ru.mag.db.jdbc.app.repository.DeliveryRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class DeliveryReportController {

    @FXML private TableView<DeliveryReportRow> tblReport;
    @FXML private TableColumn<DeliveryReportRow, Integer> colDeliveryId;
    @FXML private TableColumn<DeliveryReportRow, String> colDate;
    @FXML private TableColumn<DeliveryReportRow, String> colDocNo;
    @FXML private TableColumn<DeliveryReportRow, String> colProductName;
    @FXML private TableColumn<DeliveryReportRow, String> colForm;
    @FXML private TableColumn<DeliveryReportRow, Double> colQty;
    @FXML private TableColumn<DeliveryReportRow, Double> colPrice;
    @FXML private TableColumn<DeliveryReportRow, Double> colTotal;

    private final DeliveryRepository deliveryRepo = new DeliveryRepository();

    @FXML
    public void initialize() {
        colDeliveryId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDeliveryId()).asObject());
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDeliveryDate().toString()));
        colDocNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocNo()));
        colProductName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getProductName()));
        colForm.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getForm()));
        colQty.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getQty()).asObject());
        colPrice.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getUnitPrice()).asObject());
        colTotal.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getTotal()).asObject());

        loadReport();
    }

    @FXML
    private void loadReport() {
        try {
            List<DeliveryReportRow> data = deliveryRepo.getDeliveryReport();
            tblReport.getItems().setAll(data);
        } catch (Exception e) {
            showError("Error loading report: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("System Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}