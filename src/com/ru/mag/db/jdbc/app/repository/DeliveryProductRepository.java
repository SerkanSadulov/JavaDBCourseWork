package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.DeliveryProduct;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProductRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_SQL =
            "INSERT INTO Delivery_Product(delivery_product_id, delivery_id, product_id, batch_id, qty, unit_price) VALUES (?,?,?,?,?,?)";
    private static final String UPDATE_SQL =
            "UPDATE Delivery_Product SET delivery_id=?, product_id=?, batch_id=?, qty=?, unit_price=? WHERE delivery_product_id=?";
    private static final String DELETE_SQL =
            "DELETE FROM Delivery_Product WHERE delivery_product_id=?";
    private static final String FIND_ALL_SQL =
            "SELECT delivery_product_id, delivery_id, product_id, batch_id, qty, unit_price FROM Delivery_Product ORDER BY delivery_product_id";

    private final PreparedStatement insertStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;
    private final PreparedStatement findAllStmt;

    public DeliveryProductRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DeliveryProductRepository", e);
        }
    }

    public void create(DeliveryProduct dp) {
        try {
            insertStmt.setInt(1, dp.getDeliveryProductId());
            insertStmt.setInt(2, dp.getDeliveryId());
            insertStmt.setInt(3, dp.getProductId());
            insertStmt.setInt(4, dp.getBatchId());
            insertStmt.setDouble(5, dp.getQty());
            insertStmt.setDouble(6, dp.getUnitPrice());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(DeliveryProduct dp) {
        try {
            updateStmt.setInt(1, dp.getDeliveryId());
            updateStmt.setInt(2, dp.getProductId());
            updateStmt.setInt(3, dp.getBatchId());
            updateStmt.setDouble(4, dp.getQty());
            updateStmt.setDouble(5, dp.getUnitPrice());
            updateStmt.setInt(6, dp.getDeliveryProductId());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int deliveryProductId) {
        try {
            deleteStmt.setInt(1, deliveryProductId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeliveryProduct> findAll() {
        List<DeliveryProduct> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
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