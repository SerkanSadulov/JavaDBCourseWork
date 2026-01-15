package com.ru.mag.db.jdbc.app.repository;


import com.ru.mag.db.jdbc.app.domain.*;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public void create(Product p) {
        Connection c = DBConnection.getConnection();

        try {
            c.setAutoCommit(false);

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "INSERT INTO Product(product_id, manufacturer_id, name, form, gtin) " +
                                         "VALUES (?, ?, ?, ?, ?)")) {

                ps.setInt(1, p.getProductId());
                ps.setInt(2, p.getManufacturerId());
                ps.setString(3, p.getName());
                ps.setString(4, p.getForm());
                ps.setString(5, p.getGtin());
                ps.executeUpdate();
            }

            if (p.getType() == ProductType.MEDICINE) {
                try (PreparedStatement ps =
                             c.prepareStatement(
                                     "INSERT INTO Medicine(product_id, rx_flag) VALUES (?, ?)")) {

                    ps.setInt(1, p.getProductId());
                    ps.setBoolean(2, p.getRxFlag());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps =
                             c.prepareStatement(
                                     "INSERT INTO Device(product_id, risk_class) VALUES (?, ?)")) {

                    ps.setInt(1, p.getProductId());
                    ps.setString(2, p.getRiskClass());
                    ps.executeUpdate();
                }
            }

            for (IngredientAmount ia : p.getIngredients()) {
                try (PreparedStatement ps =
                             c.prepareStatement(
                                     "INSERT INTO Product_Ingredient(product_id, ingredient_id, amount, unit) " +
                                             "VALUES (?, ?, ?, ?)")) {

                    ps.setInt(1, p.getProductId());
                    ps.setInt(2, ia.getIngredientId());
                    ps.setDouble(3, ia.getAmount());
                    ps.setString(4, ia.getUnit());
                    ps.executeUpdate();
                }
            }

            ProductPrice price = p.getCurrentPrice();
            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "INSERT INTO Product_Price(price_id, product_id, valid_from, valid_to, price) " +
                                         "VALUES (?, ?, ?, ?, ?)")) {

                ps.setInt(1, price.getPriceId());
                ps.setInt(2, p.getProductId());
                ps.setDate(3, Date.valueOf(price.getValidFrom()));
                ps.setDate(4, price.getValidTo() == null ? null : Date.valueOf(price.getValidTo()));
                ps.setDouble(5, price.getPrice());
                ps.executeUpdate();
            }

            c.commit();

        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Product creation failed", e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();

        String sql =
                "SELECT p.product_id, p.manufacturer_id, p.name, p.form, p.gtin, " +
                        "       CASE WHEN m.product_id IS NOT NULL THEN 'MEDICINE' ELSE 'DEVICE' END AS type, " +
                        "       m.rx_flag, d.risk_class " +
                        "FROM Product p " +
                        "LEFT JOIN Medicine m ON m.product_id = p.product_id " +
                        "LEFT JOIN Device d ON d.product_id = p.product_id";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductType type = ProductType.valueOf(rs.getString("type"));

                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getInt("manufacturer_id"),
                        rs.getString("name"),
                        rs.getString("form"),
                        rs.getString("gtin"),
                        type
                );

                if (type == ProductType.MEDICINE) {
                    p.setRxFlag(rs.getBoolean("rx_flag"));
                } else {
                    p.setRiskClass(rs.getString("risk_class"));
                }

                p.setIngredients(loadIngredients(p.getProductId()));
                p.setCurrentPrice(loadCurrentPrice(p.getProductId()));

                list.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<IngredientAmount> loadIngredients(int productId) {
        List<IngredientAmount> list = new ArrayList<>();

        String sql =
                "SELECT i.ingredient_id, i.name, pi.amount, pi.unit " +
                        "FROM Product_Ingredient pi " +
                        "JOIN Ingredient i ON i.ingredient_id = pi.ingredient_id " +
                        "WHERE pi.product_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new IngredientAmount(
                        rs.getInt("ingredient_id"),
                        rs.getString("name"),
                        rs.getDouble("amount"),
                        rs.getString("unit")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public ProductPrice loadCurrentPrice(int productId) {
        String sql =
                "SELECT price_id, valid_from, valid_to, price " +
                        "FROM Product_Price " +
                        "WHERE product_id = ? " +
                        "AND valid_from <= SYSDATE " +
                        "AND (valid_to IS NULL OR valid_to >= SYSDATE) " +
                        "ORDER BY valid_from DESC FETCH FIRST 1 ROW ONLY";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ProductPrice(
                        rs.getInt("price_id"),
                        productId,
                        rs.getDate("valid_from").toLocalDate(),
                        rs.getDate("valid_to") == null ? null :
                                rs.getDate("valid_to").toLocalDate(),
                        rs.getDouble("price")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Product p) {
        String sql =
                "UPDATE Product SET " +
                        "manufacturer_id = ?, " +
                        "name = ?, " +
                        "form = ?, " +
                        "gtin = ? " +
                        "WHERE product_id = ?";

        try (PreparedStatement ps =
                     DBConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, p.getManufacturerId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getForm());
            ps.setString(4, p.getGtin());
            ps.setInt(5, p.getProductId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public void delete(int productId) {
        Connection c = DBConnection.getConnection();

        try {
            c.setAutoCommit(false);

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "DELETE FROM Product_Ingredient WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "DELETE FROM Product_Price WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "DELETE FROM Medicine WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "DELETE FROM Device WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         c.prepareStatement(
                                 "DELETE FROM Product WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            c.commit();

        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }
}



