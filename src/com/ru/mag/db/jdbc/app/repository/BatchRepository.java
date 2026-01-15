package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Batch;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchRepository {

    public void create(Batch b) {
        String sql = "INSERT INTO Batch(batch_id, product_id, batch_no, expiry_date) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, b.getBatchId());
            ps.setInt(2, b.getProductId());
            ps.setString(3, b.getBatchNo());
            ps.setDate(4, Date.valueOf(b.getExpiryDate()));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Batch b) {
        String sql = "UPDATE Batch SET product_id=?, batch_no=?, expiry_date=? WHERE batch_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, b.getProductId());
            ps.setString(2, b.getBatchNo());
            ps.setDate(3, Date.valueOf(b.getExpiryDate()));
            ps.setInt(4, b.getBatchId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int batchId) {
        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Batch WHERE batch_id=?")) {
            ps.setInt(1, batchId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Batch> findAll() {
        List<Batch> list = new ArrayList<>();
        String sql = "SELECT batch_id, product_id, batch_no, expiry_date FROM Batch ORDER BY batch_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Batch b = new Batch();
                b.setBatchId(rs.getInt("batch_id"));
                b.setProductId(rs.getInt("product_id"));
                b.setBatchNo(rs.getString("batch_no"));
                b.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                list.add(b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
