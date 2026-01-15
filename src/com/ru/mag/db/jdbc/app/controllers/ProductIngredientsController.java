package com.ru.mag.db.jdbc.app.controllers;

import com.ru.mag.db.jdbc.app.domain.Ingredient;
import com.ru.mag.db.jdbc.app.domain.IngredientAmount;
import com.ru.mag.db.jdbc.app.domain.Product;
import com.ru.mag.db.jdbc.app.repository.IngredientRepository;
import com.ru.mag.db.jdbc.app.repository.ProductIngredientRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProductIngredientsController {

    @FXML private Label lblProduct;

    @FXML private ComboBox<Ingredient> cmbIngredient;
    @FXML private TextField txtAmount;
    @FXML private TextField txtUnit;

    @FXML private TableView<IngredientAmount> tblIngredients;
    @FXML private TableColumn<IngredientAmount, Integer> colIngredientId;
    @FXML private TableColumn<IngredientAmount, String> colIngredientName;
    @FXML private TableColumn<IngredientAmount, Double> colAmount;
    @FXML private TableColumn<IngredientAmount, String> colUnit;

    private int productId;

    private final ProductIngredientRepository repo = new ProductIngredientRepository();
    private final IngredientRepository ingredientRepo = new IngredientRepository();

    @FXML
    public void initialize() {
        colIngredientId.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getIngredientId()).asObject());

        colIngredientName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getIngredientName()));

        colAmount.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getAmount()).asObject());

        colUnit.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getUnit()));

        cmbIngredient.getItems().setAll(ingredientRepo.findAll());
    }

    public void setProduct(Product p) {
        this.productId = p.getProductId();
        lblProduct.setText("Product: " + p.getProductId() + " - " + p.getName());
        load();
    }

    private void load() {
        tblIngredients.getItems().setAll(repo.findByProduct(productId));
    }

    @FXML
    private void onSelect() {
        IngredientAmount sel = tblIngredients.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        // set combo selection by ingredientId
        cmbIngredient.getItems().stream()
                .filter(i -> i.getIngredientId() == sel.getIngredientId())
                .findFirst()
                .ifPresent(cmbIngredient::setValue);

        txtAmount.setText(String.valueOf(sel.getAmount()));
        txtUnit.setText(sel.getUnit());
    }

    @FXML
    private void addIngredient() {
        try {
            Ingredient ing = cmbIngredient.getValue();
            if (ing == null) {
                showError("Select ingredient");
                return;
            }
            if (txtAmount.getText().isBlank() || txtUnit.getText().isBlank()) {
                showError("Amount and unit required");
                return;
            }

            repo.add(productId,
                    ing.getIngredientId(),
                    Double.parseDouble(txtAmount.getText()),
                    txtUnit.getText()
            );

            clearInputs();
            load();
            showInfo("Ingredient added");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateIngredient() {
        IngredientAmount sel = tblIngredients.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Select ingredient row");
            return;
        }

        try {
            if (txtAmount.getText().isBlank() || txtUnit.getText().isBlank()) {
                showError("Amount and unit required");
                return;
            }

            repo.update(productId,
                    sel.getIngredientId(),
                    Double.parseDouble(txtAmount.getText()),
                    txtUnit.getText()
            );

            clearInputs();
            load();
            showInfo("Ingredient updated");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void deleteIngredient() {
        IngredientAmount sel = tblIngredients.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Select ingredient row");
            return;
        }

        if (!confirm("Delete ingredient " + sel.getIngredientName() + "?")) return;

        repo.delete(productId, sel.getIngredientId());
        clearInputs();
        load();
        showInfo("Ingredient deleted");
    }

    private void clearInputs() {
        cmbIngredient.getSelectionModel().clearSelection();
        txtAmount.clear();
        txtUnit.clear();
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
