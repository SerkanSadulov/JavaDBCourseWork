package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Inventory;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository implements AutoCloseable {

    private final Connection connection;

    private static final String UPSERT_SQL = """
            MERGE INTO Inventory inv
            USING dual
            ON (inv.batch_id = ?)
            WHEN MATCHED THEN
                UPDATE SET qty = ?, min_qty = ?
            WHEN NOT MATCHED THEN
                INSERT (batch_id, qty, min_qty) VALUES (?, ?, ?)
            """;

    private static final String DELETE_SQL = "DELETE FROM Inventory WHERE batch_id=?";
    private static final String FIND_ALL_SQL = "SELECT batch_id, qty, min_qty FROM Inventory ORDER BY batch_id";

    private final PreparedStatement upsertStmt;
    private final PreparedStatement deleteStmt;
    private final PreparedStatement findAllStmt;

    public InventoryRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.upsertStmt = connection.prepareStatement(UPSERT_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void upsert(Inventory i) {
        try {
            upsertStmt.setInt(1, i.getBatchId());
            upsertStmt.setDouble(2, i.getQty());
            upsertStmt.setDouble(3, i.getMinQty());
            upsertStmt.setInt(4, i.getBatchId());
            upsertStmt.setDouble(5, i.getQty());
            upsertStmt.setDouble(6, i.getMinQty());
            upsertStmt.executeUpdate();
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

    public List<Inventory> findAll() {
        List<Inventory> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
            while (rs.next()) {
                Inventory i = new Inventory();
                i.setBatchId(rs.getInt("batch_id"));
                i.setQty(rs.getDouble("qty"));
                i.setMinQty(rs.getDouble("min_qty"));
                list.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        if (upsertStmt != null) upsertStmt.close();
        if (deleteStmt != null) deleteStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (connection != null) connection.close();
    }
}