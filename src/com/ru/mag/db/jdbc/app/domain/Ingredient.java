package com.ru.mag.db.jdbc.app.domain;

public class Ingredient {
    private int ingredientId;
    private String name;

    public Ingredient() { }

    public Ingredient(int ingredientId, String name) {
        this.ingredientId = ingredientId;
        this.name = name;
    }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return ingredientId + " - " + name;
    }
}
