package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Inventory;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    public void upsert(Inventory i) {
        String sql = """
            MERGE INTO Inventory inv
            USING dual
            ON (inv.batch_id = ?)
            WHEN MATCHED THEN
                UPDATE SET qty = ?, min_qty = ?
            WHEN NOT MATCHED THEN
                INSERT (batch_id, qty, min_qty) VALUES (?, ?, ?)
            """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, i.getBatchId());
            ps.setDouble(2, i.getQty());
            ps.setDouble(3, i.getMinQty());
            ps.setInt(4, i.getBatchId());
            ps.setDouble(5, i.getQty());
            ps.setDouble(6, i.getMinQty());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int batchId) {
        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Inventory WHERE batch_id=?")) {
            ps.setInt(1, batchId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Inventory> findAll() {
        List<Inventory> list = new ArrayList<>();

        String sql = "SELECT batch_id, qty, min_qty FROM Inventory ORDER BY batch_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
}
