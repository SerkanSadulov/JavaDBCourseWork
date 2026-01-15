package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Delivery;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRepository {

    public void create(Delivery d) {
        String sql =
                "INSERT INTO Delivery(delivery_id, customer_id, doc_no, delivered_at, vehicle_plate, carrier, note) " +
                        "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, d.getDeliveryId());
            ps.setInt(2, d.getCustomerId());
            ps.setString(3, d.getDocNo());
            ps.setDate(4, Date.valueOf(d.getDeliveredAt()));
            ps.setString(5, d.getVehiclePlate());
            ps.setString(6, d.getCarrier());
            ps.setString(7, d.getNote());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Delivery d) {
        String sql =
                "UPDATE Delivery SET customer_id=?, doc_no=?, delivered_at=?, vehicle_plate=?, carrier=?, note=? " +
                        "WHERE delivery_id=?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, d.getCustomerId());
            ps.setString(2, d.getDocNo());
            ps.setDate(3, Date.valueOf(d.getDeliveredAt()));
            ps.setString(4, d.getVehiclePlate());
            ps.setString(5, d.getCarrier());
            ps.setString(6, d.getNote());
            ps.setInt(7, d.getDeliveryId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int deliveryId) {
        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Delivery WHERE delivery_id=?")) {
            ps.setInt(1, deliveryId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Delivery> findAll() {
        List<Delivery> list = new ArrayList<>();

        String sql =
                "SELECT delivery_id, customer_id, doc_no, delivered_at, vehicle_plate, carrier, note " +
                        "FROM Delivery ORDER BY delivery_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
