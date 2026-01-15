package com.ru.mag.db.jdbc.app.domain;

public class IngredientAmount {
    private int ingredientId;
    private String ingredientName;
    private double amount;
    private String unit;

    public IngredientAmount(int ingredientId, String ingredientName, double amount, String unit) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.amount = amount;
        this.unit = unit;
    }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
