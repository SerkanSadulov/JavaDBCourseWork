package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Product;
import com.ru.mag.db.jdbc.app.domain.ProductPrice;
import com.ru.mag.db.jdbc.app.repository.ProductPriceRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ProductPriceController {

    @FXML private Label lblProduct;

    @FXML private TextField txtPriceId;
    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private TextField txtPrice;

    @FXML private TableView<ProductPrice> tblPrices;
    @FXML private TableColumn<ProductPrice, Integer> colPriceId;
    @FXML private TableColumn<ProductPrice, LocalDate> colFrom;
    @FXML private TableColumn<ProductPrice, LocalDate> colTo;
    @FXML private TableColumn<ProductPrice, Double> colPrice;

    private final ProductPriceRepository repo = new ProductPriceRepository();

    private int productId;

    @FXML
    public void initialize() {
        colPriceId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPriceId()).asObject());
        colFrom.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getValidFrom()));
        colTo.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getValidTo()));
        colPrice.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getPrice()).asObject());
    }

    public void setProduct(Product p) {
        this.productId = p.getProductId();
        lblProduct.setText("Product: " + p.getProductId() + " - " + p.getName());
        load();
    }

    private void load() {
        tblPrices.getItems().setAll(repo.findByProduct(productId));
    }

    @FXML
    private void addPrice() {
        try {
            if (dpFrom.getValue() == null) {
                showError("Valid from is required");
                return;
            }
            if (txtPrice.getText().isBlank()) {
                showError("Price is required");
                return;
            }

            ProductPrice pp = new ProductPrice(
                    Integer.parseInt(txtPriceId.getText()),
                    productId,
                    dpFrom.getValue(),
                    dpTo.getValue(),
                    Double.parseDouble(txtPrice.getText())
            );

            repo.create(pp);
            clearInputs();
            load();
            showInfo("Price added");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void deletePrice() {
        ProductPrice sel = tblPrices.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Select price row");
            return;
        }

        if (!confirm("Delete price ID " + sel.getPriceId() + "?")) return;

        repo.deleteById(sel.getPriceId());
        load();
        showInfo("Price deleted");
    }

    private void clearInputs() {
        txtPriceId.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
        txtPrice.clear();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
