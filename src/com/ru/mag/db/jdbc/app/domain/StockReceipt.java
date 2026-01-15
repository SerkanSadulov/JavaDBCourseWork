package com.ru.mag.db.jdbc.app.domain;

public class StockReceipt {
    private int receiptId;
    private int supplierId;
    private String docNo;
    private java.time.LocalDate receivedAt;
    private String note;

    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int receiptId) { this.receiptId = receiptId; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getDocNo() { return docNo; }
    public void setDocNo(String docNo) { this.docNo = docNo; }
    public java.time.LocalDate getReceivedAt() { return receivedAt; }
    public void setReceivedAt(java.time.LocalDate receivedAt) { this.receivedAt = receivedAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
