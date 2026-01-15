package com.ru.mag.db.jdbc.app.domain;

public class Company {

    protected int companyId;
    protected String name;
    protected String eik;
    protected String phone;

    public Company(int companyId, String name, String eik, String phone) {
        this.companyId = companyId;
        this.name = name;
        this.eik = eik;
        this.phone = phone;
    }

    public int getCompanyId() { return companyId; }
    public String getName() { return name; }
    public String getEik() { return eik; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setEik(String eik) { this.eik = eik; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return name;
    }
}