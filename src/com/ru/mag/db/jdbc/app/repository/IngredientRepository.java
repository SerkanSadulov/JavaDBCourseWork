package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.Ingredient;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_SQL = "INSERT INTO Ingredient(ingredient_id, name) VALUES (?, ?)";
    private static final String FIND_ALL_SQL = "SELECT ingredient_id, name FROM Ingredient ORDER BY name";
    private static final String UPDATE_SQL = "UPDATE Ingredient SET name=? WHERE ingredient_id=?";
    private static final String DELETE_SQL = "DELETE FROM Ingredient WHERE ingredient_id=?";

    private final PreparedStatement insertStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement updateStmt;
    private final PreparedStatement deleteStmt;

    public IngredientRepository() {
        try {
            this.connection = DBConnection.getConnection();
            this.insertStmt = connection.prepareStatement(INSERT_SQL);
            this.findAllStmt = connection.prepareStatement(FIND_ALL_SQL);
            this.updateStmt = connection.prepareStatement(UPDATE_SQL);
            this.deleteStmt = connection.prepareStatement(DELETE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize IngredientRepository", e);
        }
    }

    public void create(Ingredient i) {
        try {
            insertStmt.setInt(1, i.getIngredientId());
            insertStmt.setString(2, i.getName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Create ingredient failed", e);
        }
    }

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setIngredientId(rs.getInt("ingredient_id"));
                i.setName(rs.getString("name"));
                list.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find all ingredients failed", e);
        }
        return list;
    }

    public int update(Ingredient i) {
        try {
            updateStmt.setString(1, i.getName());
            updateStmt.setInt(2, i.getIngredientId());
            return updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update ingredient failed", e);
        }
    }

    public void delete(int ingredientId) {
        try {
            deleteStmt.setInt(1, ingredientId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete ingredient failed", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (insertStmt != null) insertStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (updateStmt != null) updateStmt.close();
        if (deleteStmt != null) deleteStmt.close();
        if (connection != null) connection.close();
    }
}