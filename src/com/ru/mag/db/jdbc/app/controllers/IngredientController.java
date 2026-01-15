package com.ru.mag.db.jdbc.app.controllers;


import com.ru.mag.db.jdbc.app.domain.Ingredient;
import com.ru.mag.db.jdbc.app.repository.IngredientRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class IngredientController {

    @FXML private TextField txtIngredientId;
    @FXML private TextField txtName;

    @FXML private TableView<Ingredient> tblIngredients;
    @FXML private TableColumn<Ingredient, Integer> colId;
    @FXML private TableColumn<Ingredient, String> colName;

    private final IngredientRepository repo = new IngredientRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIngredientId()).asObject());
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        load();
    }

    private void load() {
        tblIngredients.getItems().setAll(repo.findAll());
    }

    @FXML
    private void create() {
        repo.create(new Ingredient(
                Integer.parseInt(txtIngredientId.getText()),
                txtName.getText()
        ));
        clear();
        load();
    }

    @FXML
    private void onSelect() {
        Ingredient i = tblIngredients.getSelectionModel().getSelectedItem();
        if (i == null) return;
        txtIngredientId.setText(String.valueOf(i.getIngredientId()));
        txtIngredientId.setDisable(true);
        txtName.setText(i.getName());
    }

    @FXML
    private void update() {
        Ingredient i = tblIngredients.getSelectionModel().getSelectedItem();
        i.setName(txtName.getText());
        repo.update(i);
        load();
    }

    @FXML
    private void delete() {
        Ingredient i = tblIngredients.getSelectionModel().getSelectedItem();
        repo.delete(i.getIngredientId());
        clear();
        load();
    }

    private void clear() {
        txtIngredientId.clear();
        txtIngredientId.setDisable(false);
        txtName.clear();
    }
}
