package com.ru.mag.db.jdbc.app.domain;

import java.time.LocalDate;

public class DeliveryReportRow {
    private final int deliveryId;
    private final LocalDate deliveryDate;
    private final String docNo;
    private final String productName;
    private final String form;
    private final double qty;
    private final double unitPrice;
    private final double total;

    public DeliveryReportRow(int deliveryId, LocalDate deliveryDate, String docNo,
                             String productName, String form, double qty, double unitPrice) {
        this.deliveryId = deliveryId;
        this.deliveryDate = deliveryDate;
        this.docNo = docNo;
        this.productName = productName;
        this.form = form;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = qty * unitPrice;
    }

    public int getDeliveryId() { return deliveryId; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getDocNo() { return docNo; }
    public String getProductName() { return productName; }
    public String getForm() { return form; }
    public double getQty() { return qty; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
}