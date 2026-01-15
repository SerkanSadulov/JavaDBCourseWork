package com.ru.mag.db.jdbc.app.domain;

import java.time.LocalDate;

public class ProductPrice {

    private int priceId;
    private int productId;
    private LocalDate validFrom;
    private LocalDate validTo;
    private double price;

    public ProductPrice() {}

    public ProductPrice(int priceId, int productId,
                        LocalDate validFrom, LocalDate validTo,
                        double price) {
        this.priceId = priceId;
        this.productId = productId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.price = price;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
