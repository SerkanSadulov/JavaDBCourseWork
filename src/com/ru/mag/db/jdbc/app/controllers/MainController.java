package com.ru.mag.db.jdbc.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private TextField txtProductName;

    public void openProductCrud(ActionEvent event) throws IOException {
        openCrudWindow("ProductCrud.fxml", "Products");
    }

    public void openManufacturerCrud(ActionEvent event) throws IOException {
        openCrudWindow("ManufacturerCrud.fxml", "Manufacturers");
    }

    public void openCustomerCrud(ActionEvent event) throws IOException {
        openCrudWindow("CustomerCrud.fxml", "Customers");
    }

    public void openIngredientCrud(ActionEvent event) throws IOException {
        openCrudWindow("IngredientCrud.fxml", "Ingredients");
    }

    public void openSupplierCrud(ActionEvent event) throws IOException {
        openCrudWindow("SupplierCrud.fxml", "Suppliers");
    }

    public void openBatchCrud(ActionEvent e) throws IOException {
        openCrudWindow("BatchCrud.fxml", "Batches");
    }

    public void openInventoryCrud(ActionEvent e) throws IOException {
        openCrudWindow("InventoryCrud.fxml", "Inventory");
    }

    public void openStockReceiptCrud(ActionEvent e) throws IOException {
        openCrudWindow("StockReceiptCrud.fxml", "Stock Receipts");
    }

    public void openDeliveryCrud(ActionEvent e) throws IOException {
        openCrudWindow("DeliveryCrud.fxml", "Deliveries");
    }

    public void openDeliveryProductCrud(ActionEvent e) throws IOException {
        openCrudWindow("DeliveryProductCrud.fxml", "Delivery Products");
    }
    public void openDeliveryReport(ActionEvent e) throws IOException {
        openCrudWindow("DeliveryReport.fxml", "Delivery Report");
    }

    private void openCrudWindow(String fxml, String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/ru/mag/db/jdbc/app/views/" + fxml
                )
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
