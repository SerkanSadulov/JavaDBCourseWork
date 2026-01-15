package com.ru.mag.db.jdbc.app.domain;

public class ProductItem {
    public final int id;
    public final int manufacturerId;
    public final String name;
    public final String form;
    public final String gtin;

    public ProductItem(int id, int manufacturerId, String name, String form, String gtin) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.form = form;
        this.gtin = gtin;
    }
}
