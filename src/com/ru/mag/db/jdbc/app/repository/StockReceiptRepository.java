package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.StockReceipt;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockReceiptRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_SQL = "INSERT INTO Stock_Receipt(receipt_id, supplier_id, doc_no, received_at, note) VALUES (?,?,?,?,?)";
    private static final String UPDATE_SQL = "UPDATE Stock_Receipt SET supplier_id=?, doc_no=?, received_at=?, note=? WHERE receipt_id=?";
    private static final String DELETE_SQL = "DELETE FROM Stock_Receipt WHERE receipt_id=?";
    private static final String FIND_ALL_SQL = "SELECT receipt_id, supplier_id, doc_no, received_at, note FROM Stock_Receipt ORDER BY receipt_id";

    private final PreparedStatement insertStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;
    private final PreparedStatement findAllStmt;

    public StockReceiptRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(StockReceipt r) {
        try {
            insertStmt.setInt(1, r.getReceiptId());
            insertStmt.setInt(2, r.getSupplierId());
            insertStmt.setString(3, r.getDocNo());
            insertStmt.setDate(4, Date.valueOf(r.getReceivedAt()));
            insertStmt.setString(5, r.getNote());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(StockReceipt r) {
        try {
            updateStmt.setInt(1, r.getSupplierId());
            updateStmt.setString(2, r.getDocNo());
            updateStmt.setDate(3, Date.valueOf(r.getReceivedAt()));
            updateStmt.setString(4, r.getNote());
            updateStmt.setInt(5, r.getReceiptId());
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int receiptId) {
        try {
            deleteStmt.setInt(1, receiptId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockReceipt> findAll() {
        List<StockReceipt> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
            while (rs.next()) {
                StockReceipt r = new StockReceipt();
                r.setReceiptId(rs.getInt("receipt_id"));
                r.setSupplierId(rs.getInt("supplier_id"));
                r.setDocNo(rs.getString("doc_no"));
                r.setReceivedAt(rs.getDate("received_at").toLocalDate());
                r.setNote(rs.getString("note"));
                list.add(r);
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