package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Supplier;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_COMPANY = "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)";
    private static final String INSERT_SUPPLIER = "INSERT INTO Supplier(company_id) VALUES (?)";
    private static final String FIND_ALL = "SELECT c.company_id, c.name, c.eik, c.phone FROM Supplier s JOIN Company c ON c.company_id = s.company_id";
    private static final String UPDATE_COMPANY = "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?";
    private static final String DELETE_SUPPLIER = "DELETE FROM Supplier WHERE company_id=?";
    private static final String DELETE_COMPANY = "DELETE FROM Company WHERE company_id=?";

    private final PreparedStatement insertCompanyStmt;
    private final PreparedStatement insertSupplierStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement updateCompanyStmt;
    private final PreparedStatement deleteSupplierStmt;
    private final PreparedStatement deleteCompanyStmt;

    public SupplierRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertCompanyStmt = connection.prepareStatement(INSERT_COMPANY);
            this.insertSupplierStmt = connection.prepareStatement(INSERT_SUPPLIER);
            this.findAllStmt = connection.prepareStatement(FIND_ALL);
            this.updateCompanyStmt = connection.prepareStatement(UPDATE_COMPANY);
            this.deleteSupplierStmt = connection.prepareStatement(DELETE_SUPPLIER);
            this.deleteCompanyStmt = connection.prepareStatement(DELETE_COMPANY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Supplier s) {
        try {
            connection.setAutoCommit(false);

            insertCompanyStmt.setInt(1, s.getCompanyId());
            insertCompanyStmt.setString(2, s.getName());
            insertCompanyStmt.setString(3, s.getEik());
            insertCompanyStmt.setString(4, s.getPhone());
            insertCompanyStmt.executeUpdate();

            insertSupplierStmt.setInt(1, s.getCompanyId());
            insertSupplierStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Create supplier failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
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
        try {
            updateCompanyStmt.setString(1, s.getName());
            updateCompanyStmt.setString(2, s.getEik());
            updateCompanyStmt.setString(3, s.getPhone());
            updateCompanyStmt.setInt(4, s.getCompanyId());
            return updateCompanyStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update supplier failed", e);
        }
    }

    public void delete(int companyId) {
        try {
            connection.setAutoCommit(false);

            deleteSupplierStmt.setInt(1, companyId);
            deleteSupplierStmt.executeUpdate();

            deleteCompanyStmt.setInt(1, companyId);
            deleteCompanyStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Delete supplier failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void close() throws Exception {
        if (insertCompanyStmt != null) insertCompanyStmt.close();
        if (insertSupplierStmt != null) insertSupplierStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (updateCompanyStmt != null) updateCompanyStmt.close();
        if (deleteSupplierStmt != null) deleteSupplierStmt.close();
        if (deleteCompanyStmt != null) deleteCompanyStmt.close();
        if (connection != null) connection.close();
    }
}