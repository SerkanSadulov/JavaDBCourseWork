package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Manufacturer;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerRepository {

    public void create(Manufacturer m) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement company =
                    c.prepareStatement(
                            "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)"
                    );
            company.setInt(1, m.getCompanyId());
            company.setString(2, m.getName());
            company.setString(3, m.getEik());
            company.setString(4, m.getPhone());
            company.executeUpdate();

            PreparedStatement manufacturer =
                    c.prepareStatement(
                            "INSERT INTO Manufacturer(company_id) VALUES (?)"
                    );
            manufacturer.setInt(1, m.getCompanyId());
            manufacturer.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Create manufacturer failed", e);
        }
    }

    public List<Manufacturer> findAll() {
        List<Manufacturer> list = new ArrayList<>();

        String sql =
                "SELECT c.company_id, c.name, c.eik, c.phone " +
                        "FROM Manufacturer m JOIN Company c ON c.company_id = m.company_id";

        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Manufacturer(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("eik"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load manufacturers failed", e);
        }

        return list;
    }

    public int update(Manufacturer m) {
        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(
                             "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?"
                     )) {

            stmt.setString(1, m.getName());
            stmt.setString(2, m.getEik());
            stmt.setString(3, m.getPhone());
            stmt.setInt(4, m.getCompanyId());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update manufacturer failed", e);
        }
    }

    public void delete(int companyId) {
        try {
            Connection c = DBConnection.getConnection();

            PreparedStatement m =
                    c.prepareStatement("DELETE FROM Manufacturer WHERE company_id=?");
            m.setInt(1, companyId);
            m.executeUpdate();

            PreparedStatement comp =
                    c.prepareStatement("DELETE FROM Company WHERE company_id=?");
            comp.setInt(1, companyId);
            comp.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Delete manufacturer failed", e);
        }
    }
}
