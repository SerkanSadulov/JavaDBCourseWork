package com.ru.mag.db.jdbc.app.domain;

public class ManufacturerItem {
    public final int id;
    public final String name;

    public ManufacturerItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
