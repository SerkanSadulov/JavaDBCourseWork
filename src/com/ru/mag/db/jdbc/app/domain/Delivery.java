package com.ru.mag.db.jdbc.app.domain;

public class Delivery {
    private int deliveryId;
    private int customerId;
    private String docNo;
    private java.time.LocalDate deliveredAt;
    private String vehiclePlate;
    private String carrier;
    private String note;

    public int getDeliveryId() { return deliveryId; }
    public void setDeliveryId(int deliveryId) { this.deliveryId = deliveryId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getDocNo() { return docNo; }
    public void setDocNo(String docNo) { this.docNo = docNo; }
    public java.time.LocalDate getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(java.time.LocalDate deliveredAt) { this.deliveredAt = deliveredAt; }
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
