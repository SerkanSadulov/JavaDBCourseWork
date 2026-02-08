package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Batch;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_SQL = "INSERT INTO Batch(batch_id, product_id, batch_no, expiry_date) VALUES (?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE Batch SET product_id=?, batch_no=?, expiry_date=? WHERE batch_id=?";
    private static final String DELETE_SQL = "DELETE FROM Batch WHERE batch_id=?";
    private static final String FIND_ALL_SQL = "SELECT batch_id, product_id, batch_no, expiry_date FROM Batch ORDER BY batch_id";

    private final PreparedStatement insertStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;
    private final PreparedStatement findAllStmt;

    public BatchRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize BatchRepository", e);
        }
    }

    public void create(Batch b) {
        try {
            insertStmt.setInt(1, b.getBatchId());
            insertStmt.setInt(2, b.getProductId());
            insertStmt.setString(3, b.getBatchNo());
            insertStmt.setDate(4, Date.valueOf(b.getExpiryDate()));
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Batch b) {
        try {
            updateStmt.setInt(1, b.getProductId());
            updateStmt.setString(2, b.getBatchNo());
            updateStmt.setDate(3, Date.valueOf(b.getExpiryDate()));
            updateStmt.setInt(4, b.getBatchId());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int batchId) {
        try {
            deleteStmt.setInt(1, batchId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Batch> findAll() {
        List<Batch> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
            while (rs.next()) {
                Batch b = new Batch();
                b.setBatchId(rs.getInt("batch_id"));
                b.setProductId(rs.getInt("product_id"));
                b.setBatchNo(rs.getString("batch_no"));
                b.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                list.add(b);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        if (insertStmt != null) insertStmt.close();
        if (updateStmt != null) updateStmt.close();
        if (deleteStmt != null) deleteStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (connection != null) connection.close();
    }
}