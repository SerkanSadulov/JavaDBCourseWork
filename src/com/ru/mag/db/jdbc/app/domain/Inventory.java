package com.ru.mag.db.jdbc.app.domain;

public class Inventory {
    private int batchId;
    private double qty;
    private double minQty;

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }
    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }
    public double getMinQty() { return minQty; }
    public void setMinQty(double minQty) { this.minQty = minQty; }
}
