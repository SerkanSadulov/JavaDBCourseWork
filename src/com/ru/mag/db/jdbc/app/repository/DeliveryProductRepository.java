package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.DeliveryProduct;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProductRepository {

    public void create(DeliveryProduct dp) {
        String sql =
                "INSERT INTO Delivery_Product(" +
                        "delivery_product_id, delivery_id, product_id, batch_id, qty, unit_price) " +
                        "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, dp.getDeliveryProductId());
            ps.setInt(2, dp.getDeliveryId());
            ps.setInt(3, dp.getProductId());
            ps.setInt(4, dp.getBatchId());
            ps.setDouble(5, dp.getQty());
            ps.setDouble(6, dp.getUnitPrice());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(DeliveryProduct dp) {
        String sql =
                "UPDATE Delivery_Product SET " +
                        "delivery_id=?, product_id=?, batch_id=?, qty=?, unit_price=? " +
                        "WHERE delivery_product_id=?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, dp.getDeliveryId());
            ps.setInt(2, dp.getProductId());
            ps.setInt(3, dp.getBatchId());
            ps.setDouble(4, dp.getQty());
            ps.setDouble(5, dp.getUnitPrice());
            ps.setInt(6, dp.getDeliveryProductId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int deliveryProductId) {
        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Delivery_Product WHERE delivery_product_id=?")) {
            ps.setInt(1, deliveryProductId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeliveryProduct> findAll() {
        List<DeliveryProduct> list = new ArrayList<>();

        String sql =
                "SELECT delivery_product_id, delivery_id, product_id, batch_id, qty, unit_price " +
                        "FROM Delivery_Product ORDER BY delivery_product_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DeliveryProduct dp = new DeliveryProduct();
                dp.setDeliveryProductId(rs.getInt("delivery_product_id"));
                dp.setDeliveryId(rs.getInt("delivery_id"));
                dp.setProductId(rs.getInt("product_id"));
                dp.setBatchId(rs.getInt("batch_id"));
                dp.setQty(rs.getDouble("qty"));
                dp.setUnitPrice(rs.getDouble("unit_price"));
                list.add(dp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
