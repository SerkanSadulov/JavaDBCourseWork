package com.ru.mag.db.jdbc.app.domain;

public class DeliveryProduct {
    private int deliveryProductId;
    private int deliveryId;
    private int productId;
    private int batchId;
    private double qty;
    private double unitPrice;

    public int getDeliveryProductId() { return deliveryProductId; }
    public void setDeliveryProductId(int deliveryProductId) { this.deliveryProductId = deliveryProductId; }
    public int getDeliveryId() { return deliveryId; }
    public void setDeliveryId(int deliveryId) { this.deliveryId = deliveryId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }
    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
