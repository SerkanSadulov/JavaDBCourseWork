package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.ProductPrice;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPriceRepository {

    public List<ProductPrice> findByProduct(int productId) {
        String sql =
                "SELECT price_id, product_id, valid_from, valid_to, price " +
                        "FROM Product_Price " +
                        "WHERE product_id = ? " +
                        "ORDER BY valid_from DESC";

        List<ProductPrice> list = new ArrayList<>();

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ProductPrice(
                        rs.getInt("price_id"),
                        rs.getInt("product_id"),
                        rs.getDate("valid_from").toLocalDate(),
                        rs.getDate("valid_to") == null ? null : rs.getDate("valid_to").toLocalDate(),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void create(ProductPrice pp) {
        String sql =
                "INSERT INTO Product_Price(price_id, product_id, valid_from, valid_to, price) " +
                        "VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, pp.getPriceId());
            ps.setInt(2, pp.getProductId());
            ps.setDate(3, Date.valueOf(pp.getValidFrom()));
            ps.setDate(4, pp.getValidTo() == null ? null : Date.valueOf(pp.getValidTo()));
            ps.setDouble(5, pp.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(int priceId) {
        String sql = "DELETE FROM Product_Price WHERE price_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, priceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
