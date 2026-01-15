package com.ru.mag.db.jdbc.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productId;
    private int manufacturerId;
    private String name;
    private String form;
    private String gtin;
    private ProductType type;

    private Boolean rxFlag;

    private String riskClass;

    private List<IngredientAmount> ingredients = new ArrayList<>();
    private ProductPrice currentPrice;

    public Product() {}

    public Product(int productId,
                   int manufacturerId,
                   String name,
                   String form,
                   String gtin,
                   ProductType type) {

        this.productId = productId;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.form = form;
        this.gtin = gtin;
        this.type = type;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Boolean getRxFlag() {
        return rxFlag;
    }

    public void setRxFlag(Boolean rxFlag) {
        this.rxFlag = rxFlag;
    }

    public String getRiskClass() {
        return riskClass;
    }

    public void setRiskClass(String riskClass) {
        this.riskClass = riskClass;
    }

    public List<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(IngredientAmount ingredient) {
        this.ingredients.add(ingredient);
    }

    public void clearIngredients() {
        this.ingredients.clear();
    }

    public ProductPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPrice currentPrice) {
        this.currentPrice = currentPrice;
    }
}
