package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Customer;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_COMPANY = "INSERT INTO Company(company_id, name, eik, phone) VALUES (?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER = "INSERT INTO Customer(company_id, pharmacy_code) VALUES (?, ?)";
    private static final String FIND_ALL = "SELECT c.company_id, c.name, c.eik, c.phone, cu.pharmacy_code FROM Customer cu JOIN Company c ON c.company_id = cu.company_id";
    private static final String UPDATE_COMPANY = "UPDATE Company SET name=?, eik=?, phone=? WHERE company_id=?";
    private static final String UPDATE_CUSTOMER = "UPDATE Customer SET pharmacy_code=? WHERE company_id=?";
    private static final String DELETE_CUSTOMER = "DELETE FROM Customer WHERE company_id=?";
    private static final String DELETE_COMPANY = "DELETE FROM Company WHERE company_id=?";

    private final PreparedStatement insertCompanyStmt;
    private final PreparedStatement insertCustomerStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement updateCompanyStmt;
    private final PreparedStatement updateCustomerStmt;
    private final PreparedStatement deleteCustomerStmt;
    private final PreparedStatement deleteCompanyStmt;

    public CustomerRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertCompanyStmt = connection.prepareStatement(INSERT_COMPANY);
            this.insertCustomerStmt = connection.prepareStatement(INSERT_CUSTOMER);
            this.findAllStmt = connection.prepareStatement(FIND_ALL);
            this.updateCompanyStmt = connection.prepareStatement(UPDATE_COMPANY);
            this.updateCustomerStmt = connection.prepareStatement(UPDATE_CUSTOMER);
            this.deleteCustomerStmt = connection.prepareStatement(DELETE_CUSTOMER);
            this.deleteCompanyStmt = connection.prepareStatement(DELETE_COMPANY);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize CustomerRepository", e);
        }
    }

    public void create(Customer cst) {
        try {
            connection.setAutoCommit(false);

            insertCompanyStmt.setInt(1, cst.getCompanyId());
            insertCompanyStmt.setString(2, cst.getName());
            insertCompanyStmt.setString(3, cst.getEik());
            insertCompanyStmt.setString(4, cst.getPhone());
            insertCompanyStmt.executeUpdate();

            insertCustomerStmt.setInt(1, cst.getCompanyId());
            insertCustomerStmt.setString(2, cst.getPharmacyCode());
            insertCustomerStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Create customer failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
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
            connection.setAutoCommit(false);

            updateCompanyStmt.setString(1, cst.getName());
            updateCompanyStmt.setString(2, cst.getEik());
            updateCompanyStmt.setString(3, cst.getPhone());
            updateCompanyStmt.setInt(4, cst.getCompanyId());
            updateCompanyStmt.executeUpdate();

            updateCustomerStmt.setString(1, cst.getPharmacyCode());
            updateCustomerStmt.setInt(2, cst.getCompanyId());
            int rows = updateCustomerStmt.executeUpdate();

            connection.commit();
            return rows;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Update customer failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public void delete(int companyId) {
        try {
            connection.setAutoCommit(false);

            deleteCustomerStmt.setInt(1, companyId);
            deleteCustomerStmt.executeUpdate();

            deleteCompanyStmt.setInt(1, companyId);
            deleteCompanyStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Delete customer failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void close() throws Exception {
        if (insertCompanyStmt != null) insertCompanyStmt.close();
        if (insertCustomerStmt != null) insertCustomerStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (updateCompanyStmt != null) updateCompanyStmt.close();
        if (updateCustomerStmt != null) updateCustomerStmt.close();
        if (deleteCustomerStmt != null) deleteCustomerStmt.close();
        if (deleteCompanyStmt != null) deleteCompanyStmt.close();
        if (connection != null) connection.close();
    }
}