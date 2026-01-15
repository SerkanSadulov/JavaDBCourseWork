package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.IngredientAmount;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductIngredientRepository {

    public List<IngredientAmount> findByProduct(int productId) {
        String sql =
                "SELECT pi.product_id, pi.ingredient_id, i.name, pi.amount, pi.unit " +
                        "FROM Product_Ingredient pi " +
                        "JOIN Ingredient i ON i.ingredient_id = pi.ingredient_id " +
                        "WHERE pi.product_id = ? " +
                        "ORDER BY i.name";

        List<IngredientAmount> list = new ArrayList<>();

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

    public void add(int productId, int ingredientId, double amount, String unit) {
        String sql =
                "INSERT INTO Product_Ingredient(product_id, ingredient_id, amount, unit) " +
                        "VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, ingredientId);
            ps.setDouble(3, amount);
            ps.setString(4, unit);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int productId, int ingredientId, double amount, String unit) {
        String sql =
                "UPDATE Product_Ingredient SET amount = ?, unit = ? " +
                        "WHERE product_id = ? AND ingredient_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, unit);
            ps.setInt(3, productId);
            ps.setInt(4, ingredientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int productId, int ingredientId) {
        String sql =
                "DELETE FROM Product_Ingredient WHERE product_id = ? AND ingredient_id = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, ingredientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
