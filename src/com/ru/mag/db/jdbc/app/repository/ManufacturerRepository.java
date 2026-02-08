package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Manufacturer;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_COMPANY = "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)";
    private static final String INSERT_MANUFACTURER = "INSERT INTO Manufacturer(company_id) VALUES (?)";
    private static final String FIND_ALL = "SELECT c.company_id, c.name, c.eik, c.phone FROM Manufacturer m JOIN Company c ON c.company_id = m.company_id";
    private static final String UPDATE_COMPANY = "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?";
    private static final String DELETE_MANUFACTURER = "DELETE FROM Manufacturer WHERE company_id=?";
    private static final String DELETE_COMPANY = "DELETE FROM Company WHERE company_id=?";

    private final PreparedStatement insertCompanyStmt;
    private final PreparedStatement insertManufacturerStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement updateCompanyStmt;
    private final PreparedStatement deleteManufacturerStmt;
    private final PreparedStatement deleteCompanyStmt;

    public ManufacturerRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertCompanyStmt = connection.prepareStatement(INSERT_COMPANY);
            this.insertManufacturerStmt = connection.prepareStatement(INSERT_MANUFACTURER);
            this.findAllStmt = connection.prepareStatement(FIND_ALL);
            this.updateCompanyStmt = connection.prepareStatement(UPDATE_COMPANY);
            this.deleteManufacturerStmt = connection.prepareStatement(DELETE_MANUFACTURER);
            this.deleteCompanyStmt = connection.prepareStatement(DELETE_COMPANY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Manufacturer m) {
        try {
            connection.setAutoCommit(false);

            insertCompanyStmt.setInt(1, m.getCompanyId());
            insertCompanyStmt.setString(2, m.getName());
            insertCompanyStmt.setString(3, m.getEik());
            insertCompanyStmt.setString(4, m.getPhone());
            insertCompanyStmt.executeUpdate();

            insertManufacturerStmt.setInt(1, m.getCompanyId());
            insertManufacturerStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Create manufacturer failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public List<Manufacturer> findAll() {
        List<Manufacturer> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
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
        try {
            updateCompanyStmt.setString(1, m.getName());
            updateCompanyStmt.setString(2, m.getEik());
            updateCompanyStmt.setString(3, m.getPhone());
            updateCompanyStmt.setInt(4, m.getCompanyId());
            return updateCompanyStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update manufacturer failed", e);
        }
    }

    public void delete(int companyId) {
        try {
            connection.setAutoCommit(false);

            deleteManufacturerStmt.setInt(1, companyId);
            deleteManufacturerStmt.executeUpdate();

            deleteCompanyStmt.setInt(1, companyId);
            deleteCompanyStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Delete manufacturer failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void close() throws Exception {
        if (insertCompanyStmt != null) insertCompanyStmt.close();
        if (insertManufacturerStmt != null) insertManufacturerStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (updateCompanyStmt != null) updateCompanyStmt.close();
        if (deleteManufacturerStmt != null) deleteManufacturerStmt.close();
        if (deleteCompanyStmt != null) deleteCompanyStmt.close();
        if (connection != null) connection.close();
    }
}