package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.IngredientAmount;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductIngredientRepository implements AutoCloseable {

    private final Connection connection;

    private static final String FIND_BY_PRODUCT_SQL =
            "SELECT pi.product_id, pi.ingredient_id, i.name, pi.amount, pi.unit " +
                    "FROM Product_Ingredient pi " +
                    "JOIN Ingredient i ON i.ingredient_id = pi.ingredient_id " +
                    "WHERE pi.product_id = ? " +
                    "ORDER BY i.name";

    private static final String INSERT_SQL =
            "INSERT INTO Product_Ingredient(product_id, ingredient_id, amount, unit) VALUES(?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE Product_Ingredient SET amount = ?, unit = ? WHERE product_id = ? AND ingredient_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM Product_Ingredient WHERE product_id = ? AND ingredient_id = ?";

    private final PreparedStatement findByProductStmt;
    private final PreparedStatement insertStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;

    public ProductIngredientRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.findByProductStmt = connection.prepareStatement(FIND_BY_PRODUCT_SQL);
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IngredientAmount> findByProduct(int productId) {
        List<IngredientAmount> list = new ArrayList<>();
        try {
            findByProductStmt.setInt(1, productId);
            try (ResultSet rs = findByProductStmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new IngredientAmount(
                            rs.getInt("ingredient_id"),
                            rs.getString("name"),
                            rs.getDouble("amount"),
                            rs.getString("unit")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void add(int productId, int ingredientId, double amount, String unit) {
        try {
            insertStmt.setInt(1, productId);
            insertStmt.setInt(2, ingredientId);
            insertStmt.setDouble(3, amount);
            insertStmt.setString(4, unit);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int productId, int ingredientId, double amount, String unit) {
        try {
            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, unit);
            updateStmt.setInt(3, productId);
            updateStmt.setInt(4, ingredientId);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int productId, int ingredientId) {
        try {
            deleteStmt.setInt(1, productId);
            deleteStmt.setInt(2, ingredientId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (findByProductStmt != null) findByProductStmt.close();
        if (insertStmt != null) insertStmt.close();
        if (updateStmt != null) updateStmt.close();
        if (deleteStmt != null) deleteStmt.close();
        if (connection != null) connection.close();
    }
}