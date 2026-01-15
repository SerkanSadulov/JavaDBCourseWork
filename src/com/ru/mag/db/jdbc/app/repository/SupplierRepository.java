package com.ru.mag.db.jdbc.app.repository;


import com.ru.mag.db.jdbc.app.domain.Supplier;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {

    public void create(Supplier s) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement company = c.prepareStatement(
                    "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)"
            );
            company.setInt(1, s.getCompanyId());
            company.setString(2, s.getName());
            company.setString(3, s.getEik());
            company.setString(4, s.getPhone());
            company.executeUpdate();

            PreparedStatement supplier = c.prepareStatement(
                    "INSERT INTO Supplier(company_id) VALUES (?)"
            );
            supplier.setInt(1, s.getCompanyId());
            supplier.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Create supplier failed", e);
        }
    }

    public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();

        String sql =
                "SELECT c.company_id, c.name, c.eik, c.phone " +
                        "FROM Supplier s JOIN Company c ON c.company_id = s.company_id";

        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Supplier(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("eik"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load suppliers failed", e);
        }

        return list;
    }

    public int update(Supplier s) {
        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(
                             "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?"
                     )) {

            stmt.setString(1, s.getName());
            stmt.setString(2, s.getEik());
            stmt.setString(3, s.getPhone());
            stmt.setInt(4, s.getCompanyId());
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Update supplier failed", e);
        }
    }

    public void delete(int companyId) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement s =
                    c.prepareStatement("DELETE FROM Supplier WHERE company_id=?");
            s.setInt(1, companyId);
            s.executeUpdate();

            PreparedStatement comp =
                    c.prepareStatement("DELETE FROM Company WHERE company_id=?");
            comp.setInt(1, companyId);
            comp.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Delete supplier failed", e);
        }
    }
}