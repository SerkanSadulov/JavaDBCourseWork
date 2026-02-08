package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.ProductPrice;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPriceRepository implements AutoCloseable {

    private final Connection connection;

    private static final String FIND_BY_PRODUCT_SQL =
            "SELECT price_id, product_id, valid_from, valid_to, price FROM Product_Price WHERE product_id = ? ORDER BY valid_from DESC";

    private static final String INSERT_SQL =
            "INSERT INTO Product_Price(price_id, product_id, valid_from, valid_to, price) VALUES(?, ?, ?, ?, ?)";

    private static final String DELETE_SQL =
            "DELETE FROM Product_Price WHERE price_id = ?";

    private final PreparedStatement findByProductStmt;
    private final PreparedStatement insertStmt;
    private final PreparedStatement deleteStmt;

    public ProductPriceRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.findByProductStmt = connection.prepareStatement(FIND_BY_PRODUCT_SQL);
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductPrice> findByProduct(int productId) {
        List<ProductPrice> list = new ArrayList<>();
        try {
            findByProductStmt.setInt(1, productId);
            try (ResultSet rs = findByProductStmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProductPrice(
                            rs.getInt("price_id"),
                            rs.getInt("product_id"),
                            rs.getDate("valid_from").toLocalDate(),
                            rs.getDate("valid_to") == null ? null : rs.getDate("valid_to").toLocalDate(),
                            rs.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void create(ProductPrice pp) {
        try {
            insertStmt.setInt(1, pp.getPriceId());
            insertStmt.setInt(2, pp.getProductId());
            insertStmt.setDate(3, Date.valueOf(pp.getValidFrom()));
            insertStmt.setDate(4, pp.getValidTo() == null ? null : Date.valueOf(pp.getValidTo()));
            insertStmt.setDouble(5, pp.getPrice());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int priceId) {
        try {
            deleteStmt.setInt(1, priceId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (findByProductStmt != null) findByProductStmt.close();
        if (insertStmt != null) insertStmt.close();
        if (deleteStmt != null) deleteStmt.close();
        if (connection != null) connection.close();
    }
}