package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Customer;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public void create(Customer cst) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement company = c.prepareStatement(
                    "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)"
            );
            company.setInt(1, cst.getCompanyId());
            company.setString(2, cst.getName());
            company.setString(3, cst.getEik());
            company.setString(4, cst.getPhone());
            company.executeUpdate();

            PreparedStatement customer = c.prepareStatement(
                    "INSERT INTO Customer(company_id, pharmacy_code) VALUES (?, ?)"
            );
            customer.setInt(1, cst.getCompanyId());
            customer.setString(2, cst.getPharmacyCode());
            customer.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Create customer failed", e);
        }
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();

        String sql =
                "SELECT c.company_id, c.name, c.eik, c.phone, cu.pharmacy_code " +
                        "FROM Customer cu JOIN Company c ON c.company_id = cu.company_id";

        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("eik"),
                        rs.getString("phone"),
                        rs.getString("pharmacy_code")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load customers failed", e);
        }

        return list;
    }

    public int update(Customer cst) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement company = c.prepareStatement(
                    "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?"
            );
            company.setString(1, cst.getName());
            company.setString(2, cst.getEik());
            company.setString(3, cst.getPhone());
            company.setInt(4, cst.getCompanyId());
            company.executeUpdate();

            PreparedStatement customer = c.prepareStatement(
                    "UPDATE Customer SET pharmacy_code=? WHERE company_id=?"
            );
            customer.setString(1, cst.getPharmacyCode());
            customer.setInt(2, cst.getCompanyId());
            return customer.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Update customer failed", e);
        }
    }

    public void delete(int companyId) {
        try {
            Connection c = DBConnection.getConnection();

            c.prepareStatement("DELETE FROM Customer WHERE company_id=" + companyId).executeUpdate();
            c.prepareStatement("DELETE FROM Company WHERE company_id=" + companyId).executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Delete customer failed", e);
        }
    }
}
