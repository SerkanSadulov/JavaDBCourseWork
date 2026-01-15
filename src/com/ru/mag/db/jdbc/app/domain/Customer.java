package com.ru.mag.db.jdbc.app.domain;

public class Customer extends Company {

    private String pharmacyCode;

    public Customer(int companyId, String name, String eik,
                    String phone, String pharmacyCode) {
        super(companyId, name, eik, phone);
        this.pharmacyCode = pharmacyCode;
    }

    public String getPharmacyCode() {
        return pharmacyCode;
    }

    public void setPharmacyCode(String pharmacyCode) {
        this.pharmacyCode = pharmacyCode;
    }
}
