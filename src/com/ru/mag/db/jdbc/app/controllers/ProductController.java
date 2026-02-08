package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Manufacturer;
import com.ru.mag.db.jdbc.app.domain.Product;
import com.ru.mag.db.jdbc.app.domain.ProductType;
import com.ru.mag.db.jdbc.app.repository.ManufacturerRepository;
import com.ru.mag.db.jdbc.app.repository.ProductRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ProductController {

    @FXML private TextField txtProductId;
    @FXML private TextField txtName;
    @FXML private TextField txtForm;
    @FXML private TextField txtGtin;
    @FXML private ComboBox<Manufacturer> cmbManufacturer;
    @FXML private ComboBox<ProductType> cmbType;
    @FXML private CheckBox chkRx;
    @FXML private TextField txtRiskClass;
    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colForm;
    @FXML private TableColumn<Product, String> colType;
    @FXML private TableColumn<Product, String> colCurrentPrice;

    private final ProductRepository productRepo = new ProductRepository();
    private final ManufacturerRepository manufacturerRepo = new ManufacturerRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getProductId()).asObject());
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colForm.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getForm()));
        colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType().name()));
        colCurrentPrice.setCellValueFactory(d -> {
            if (d.getValue().getCurrentPrice() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(String.valueOf(d.getValue().getCurrentPrice().getPrice()));
        });

        txtProductId.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null));

        txtGtin.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return (newText.matches("\\d*") && newText.length() <= 13) ? change : null;
        }));

        cmbManufacturer.getItems().setAll(manufacturerRepo.findAll());
        cmbType.getItems().setAll(ProductType.values());

        cmbType.valueProperty().addListener((obs, oldV, newV) -> {
            chkRx.setVisible(newV == ProductType.MEDICINE);
            txtRiskClass.setVisible(newV == ProductType.DEVICE);
            if (newV == ProductType.MEDICINE) txtRiskClass.clear();
            else chkRx.setSelected(false);
        });

        load();
        clear();
    }

    private void load() {
        tblProducts.getItems().setAll(productRepo.findAll());
    }

    private boolean isInputValid() {
        StringBuilder sb = new StringBuilder();
        if (txtProductId.getText().isBlank()) sb.append("- Product ID is required\n");
        if (cmbManufacturer.getValue() == null) sb.append("- Select manufacturer\n");
        if (txtName.getText().isBlank()) sb.append("- Name is required\n");
        if (txtForm.getText().isBlank()) sb.append("- Form is required\n");
        if (txtGtin.getText().length() != 13) sb.append("- GTIN must be 13 digits\n");
        if (cmbType.getValue() == null) sb.append("- Select product type\n");
        if (cmbType.getValue() == ProductType.DEVICE && txtRiskClass.getText().isBlank()) {
            sb.append("- Risk class is required for devices\n");
        }

        if (sb.length() > 0) {
            showError(sb.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void create() {
        if (!isInputValid()) return;
        try {
            Product p = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    cmbManufacturer.getValue().getCompanyId(),
                    txtName.getText().trim(),
                    txtForm.getText().trim(),
                    txtGtin.getText().trim(),
                    cmbType.getValue()
            );
            if (p.getType() == ProductType.MEDICINE) p.setRxFlag(chkRx.isSelected());
            else p.setRiskClass(txtRiskClass.getText().trim());

            productRepo.create(p);
            clear();
            load();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onSelect() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) return;

        txtProductId.setText(String.valueOf(p.getProductId()));
        txtProductId.setDisable(true);
        txtName.setText(p.getName());
        txtForm.setText(p.getForm());
        txtGtin.setText(p.getGtin());
        cmbManufacturer.getItems().stream()
                .filter(m -> m.getCompanyId() == p.getManufacturerId())
                .findFirst()
                .ifPresent(cmbManufacturer::setValue);
        cmbType.setValue(p.getType());
        cmbType.setDisable(true);

        if (p.getType() == ProductType.MEDICINE) chkRx.setSelected(Boolean.TRUE.equals(p.getRxFlag()));
        else txtRiskClass.setText(p.getRiskClass());
    }

    @FXML
    private void update() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            showError("Select product");
            return;
        }
        if (!isInputValid()) return;

        p.setManufacturerId(cmbManufacturer.getValue().getCompanyId());
        p.setName(txtName.getText().trim());
        p.setForm(txtForm.getText().trim());
        p.setGtin(txtGtin.getText().trim());
        if (p.getType() == ProductType.DEVICE) p.setRiskClass(txtRiskClass.getText().trim());
        else p.setRxFlag(chkRx.isSelected());

        productRepo.update(p);
        load();
    }

    @FXML
    private void delete() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            showError("Select product");
            return;
        }
        if (!confirm("Delete product " + p.getName() + "?")) return;
        productRepo.delete(p.getProductId());
        clear();
        load();
    }

    @FXML
    private void openPrices() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            showError("Select product");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ru/mag/db/jdbc/app/views/ProductPrice.fxml"));
            Parent root = loader.load();
            ProductPriceController controller = loader.getController();
            controller.setProduct(p);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Product prices");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            load();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void openIngredients() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            showError("Select product");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ru/mag/db/jdbc/app/views/ProductIngredients.fxml"));
            Parent root = loader.load();
            ProductIngredientsController controller = loader.getController();
            controller.setProduct(p);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Product ingredients");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            load();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void clear() {
        txtProductId.clear();
        txtProductId.setDisable(false);
        txtName.clear();
        txtForm.clear();
        txtGtin.clear();
        cmbManufacturer.getSelectionModel().clearSelection();
        cmbType.getSelectionModel().clearSelection();
        cmbType.setDisable(false);
        chkRx.setSelected(false);
        chkRx.setVisible(false);
        txtRiskClass.clear();
        txtRiskClass.setVisible(false);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}