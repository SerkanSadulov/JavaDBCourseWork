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

        colId.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getProductId()).asObject());

        colName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getName()));

        colForm.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getForm()));

        colType.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getType().name()));

        colCurrentPrice.setCellValueFactory(d -> {
            if (d.getValue().getCurrentPrice() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(String.valueOf(d.getValue().getCurrentPrice().getPrice()));
        });
        cmbManufacturer.getItems().setAll(manufacturerRepo.findAll());
        cmbType.getItems().setAll(ProductType.values());

        cmbType.valueProperty().addListener((obs, oldV, newV) -> {
            chkRx.setVisible(newV == ProductType.MEDICINE);
            txtRiskClass.setVisible(newV == ProductType.DEVICE);
        });

        load();
        clear();
    }

    private void load() {
        tblProducts.getItems().setAll(productRepo.findAll());
    }

    @FXML
    private void create() {
        try {
            Manufacturer m = cmbManufacturer.getValue();
            ProductType type = cmbType.getValue();

            if (m == null || type == null) {
                showError("Select manufacturer and product type");
                return;
            }

            Product p = new Product(
                    Integer.parseInt(txtProductId.getText()),
                    m.getCompanyId(),
                    txtName.getText(),
                    txtForm.getText(),
                    txtGtin.getText(),
                    type
            );

            if (type == ProductType.MEDICINE) {
                p.setRxFlag(chkRx.isSelected());
            } else {
                if (txtRiskClass.getText().isBlank()) {
                    showError("Risk class is required for devices");
                    return;
                }
                p.setRiskClass(txtRiskClass.getText());
            }

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

        if (p.getType() == ProductType.MEDICINE) {
            chkRx.setSelected(Boolean.TRUE.equals(p.getRxFlag()));
        } else {
            txtRiskClass.setText(p.getRiskClass());
        }
    }

    @FXML
    private void update() {
        Product p = tblProducts.getSelectionModel().getSelectedItem();
        if (p == null) {
            showError("Select product");
            return;
        }

        p.setManufacturerId(cmbManufacturer.getValue().getCompanyId());
        p.setName(txtName.getText());
        p.setForm(txtForm.getText());
        p.setGtin(txtGtin.getText());

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
            FXMLLoader loader = new FXMLLoader(
                    ProductController.class.getResource("/com/ru/mag/db/jdbc/app/views/ProductPrice.fxml")
            );
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
            FXMLLoader loader = new FXMLLoader(
                    ProductController.class.getResource("/com/ru/mag/db/jdbc/app/views/ProductIngredients.fxml")
            );
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
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg,
                ButtonType.OK, ButtonType.CANCEL);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
