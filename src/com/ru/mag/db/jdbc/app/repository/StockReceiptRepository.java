package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.StockReceipt;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockReceiptRepository {

    public void create(StockReceipt r) {
        String sql =
                "INSERT INTO Stock_Receipt(receipt_id, supplier_id, doc_no, received_at, note) " +
                        "VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getReceiptId());
            ps.setInt(2, r.getSupplierId());
            ps.setString(3, r.getDocNo());
            ps.setDate(4, Date.valueOf(r.getReceivedAt()));
            ps.setString(5, r.getNote());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(StockReceipt r) {
        String sql =
                "UPDATE Stock_Receipt SET supplier_id=?, doc_no=?, received_at=?, note=? " +
                        "WHERE receipt_id=?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getSupplierId());
            ps.setString(2, r.getDocNo());
            ps.setDate(3, Date.valueOf(r.getReceivedAt()));
            ps.setString(4, r.getNote());
            ps.setInt(5, r.getReceiptId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int receiptId) {
        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Stock_Receipt WHERE receipt_id=?")) {
            ps.setInt(1, receiptId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockReceipt> findAll() {
        List<StockReceipt> list = new ArrayList<>();

        String sql =
                "SELECT receipt_id, supplier_id, doc_no, received_at, note " +
                        "FROM Stock_Receipt ORDER BY receipt_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StockReceipt r = new StockReceipt();
                r.setReceiptId(rs.getInt("receipt_id"));
                r.setSupplierId(rs.getInt("supplier_id"));
                r.setDocNo(rs.getString("doc_no"));
                r.setReceivedAt(rs.getDate("received_at").toLocalDate());
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
