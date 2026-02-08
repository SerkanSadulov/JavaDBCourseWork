package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Delivery;
import com.ru.mag.db.jdbc.app.domain.DeliveryReportRow;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_SQL =
            "INSERT INTO Delivery(delivery_id, customer_id, doc_no, delivered_at, vehicle_plate, carrier, note) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_SQL =
            "UPDATE Delivery SET customer_id=?, doc_no=?, delivered_at=?, vehicle_plate=?, carrier=?, note=? WHERE delivery_id=?";
    private static final String DELETE_SQL =
            "DELETE FROM Delivery WHERE delivery_id=?";
    private static final String FIND_ALL_SQL =
            "SELECT delivery_id, customer_id, doc_no, delivered_at, vehicle_plate, carrier, note FROM Delivery ORDER BY delivery_id";

    private static final String REPORT_SQL = """
            SELECT d.delivery_id, d.delivered_at, d.doc_no, p.name, p.form, dp.qty, dp.unit_price
            FROM Delivery d
            JOIN Delivery_Product dp ON d.delivery_id = dp.delivery_id
            JOIN Product p ON dp.product_id = p.product_id
            ORDER BY d.delivered_at DESC
            """;

    private final PreparedStatement insertStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement reportStmt;

    public DeliveryRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
            this.reportStmt = connection.prepareStatement(REPORT_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DeliveryRepository", e);
        }
    }

    public void create(Delivery d) {
        try {
            insertStmt.setInt(1, d.getDeliveryId());
            insertStmt.setInt(2, d.getCustomerId());
            insertStmt.setString(3, d.getDocNo());
            insertStmt.setDate(4, Date.valueOf(d.getDeliveredAt()));
            insertStmt.setString(5, d.getVehiclePlate());
            insertStmt.setString(6, d.getCarrier());
            insertStmt.setString(7, d.getNote());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Delivery d) {
        try {
            updateStmt.setInt(1, d.getCustomerId());
            updateStmt.setString(2, d.getDocNo());
            updateStmt.setDate(3, Date.valueOf(d.getDeliveredAt()));
            updateStmt.setString(4, d.getVehiclePlate());
            updateStmt.setString(5, d.getCarrier());
            updateStmt.setString(6, d.getNote());
            updateStmt.setInt(7, d.getDeliveryId());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int deliveryId) {
        try {
            deleteStmt.setInt(1, deliveryId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeliveryReportRow> getDeliveryReport() {
        List<DeliveryReportRow> list = new ArrayList<>();
        try (ResultSet rs = reportStmt.executeQuery()) {
            while (rs.next()) {
                list.add(new DeliveryReportRow(
                        rs.getInt("delivery_id"),
                        rs.getDate("delivered_at").toLocalDate(),
                        rs.getString("doc_no"),
                        rs.getString("name"),
                        rs.getString("form"),
                        rs.getDouble("qty"),
                        rs.getDouble("unit_price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Report generation failed", e);
        }
        return list;
    }

    public List<Delivery> findAll() {
        List<Delivery> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
            while (rs.next()) {
                Delivery d = new Delivery();
                d.setDeliveryId(rs.getInt("delivery_id"));
                d.setCustomerId(rs.getInt("customer_id"));
                d.setDocNo(rs.getString("doc_no"));
                d.setDeliveredAt(rs.getDate("delivered_at").toLocalDate());
                d.setVehiclePlate(rs.getString("vehicle_plate"));
                d.setCarrier(rs.getString("carrier"));
                d.setNote(rs.getString("note"));
                list.add(d);
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
        if (reportStmt != null) reportStmt.close();
        if (connection != null) connection.close();
    }
}