package com.ru.mag.db.jdbc.app.repository;


import com.ru.mag.db.jdbc.app.domain.Ingredient;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {

    public void create(Ingredient i) {
        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(
                             "INSERT INTO Ingredient(ingredient_id, name) VALUES (?, ?)"
                     )) {

            stmt.setInt(1, i.getIngredientId());
            stmt.setString(2, i.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Create ingredient failed", e);
        }
    }

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT ingredient_id, name FROM Ingredient ORDER BY name";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setIngredientId(rs.getInt("ingredient_id"));
                i.setName(rs.getString("name"));
                list.add(i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public int update(Ingredient i) {
        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(
                             "UPDATE Ingredient SET name=? WHERE ingredient_id=?"
                     )) {

            stmt.setString(1, i.getName());
            stmt.setInt(2, i.getIngredientId());
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Update ingredient failed", e);
        }
    }

    public void delete(int ingredientId) {
        try (PreparedStatement stmt =
                     DBConnection.getConnection().prepareStatement(
                             "DELETE FROM Ingredient WHERE ingredient_id=?"
                     )) {

            stmt.setInt(1, ingredientId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Delete ingredient failed", e);
        }
    }
}
