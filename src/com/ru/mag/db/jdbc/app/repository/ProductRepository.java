package com.ru.mag.db.jdbc.app.repository;

import com.ru.mag.db.jdbc.app.domain.*;
import com.ru.mag.db.jdbc.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements AutoCloseable {

    private final Connection connection;

    private static final String INSERT_PRODUCT = "INSERT INTO Product(product_id, manufacturer_id, name, form, gtin) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_MEDICINE = "INSERT INTO Medicine(product_id, rx_flag) VALUES (?, ?)";
    private static final String INSERT_DEVICE = "INSERT INTO Device(product_id, risk_class) VALUES (?, ?)";
    private static final String INSERT_INGREDIENT = "INSERT INTO Product_Ingredient(product_id, ingredient_id, amount, unit) VALUES (?, ?, ?, ?)";
    private static final String INSERT_PRICE = "INSERT INTO Product_Price(price_id, product_id, valid_from, valid_to, price) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_ALL = "SELECT p.product_id, p.manufacturer_id, p.name, p.form, p.gtin, " +
            "CASE WHEN m.product_id IS NOT NULL THEN 'MEDICINE' ELSE 'DEVICE' END AS type, " +
            "m.rx_flag, d.risk_class FROM Product p " +
            "LEFT JOIN Medicine m ON m.product_id = p.product_id " +
            "LEFT JOIN Device d ON d.product_id = p.product_id";

    private static final String LOAD_INGR = "SELECT i.ingredient_id, i.name, pi.amount, pi.unit FROM Product_Ingredient pi " +
            "JOIN Ingredient i ON i.ingredient_id = pi.ingredient_id WHERE pi.product_id = ?";

    private static final String LOAD_PRICE = "SELECT price_id, valid_from, valid_to, price FROM Product_Price " +
            "WHERE product_id = ? AND valid_from <= SYSDATE AND (valid_to IS NULL OR valid_to >= SYSDATE) " +
            "ORDER BY valid_from DESC FETCH FIRST 1 ROW ONLY";

    private static final String UPDATE_PRODUCT = "UPDATE Product SET manufacturer_id = ?, name = ?, form = ?, gtin = ? WHERE product_id = ?";

    private static final String DELETE_INGR = "DELETE FROM Product_Ingredient WHERE product_id = ?";
    private static final String DELETE_PRICE = "DELETE FROM Product_Price WHERE product_id = ?";
    private static final String DELETE_MED = "DELETE FROM Medicine WHERE product_id = ?";
    private static final String DELETE_DEV = "DELETE FROM Device WHERE product_id = ?";
    private static final String DELETE_PROD = "DELETE FROM Product WHERE product_id = ?";

    private final PreparedStatement insertProductStmt;
    private final PreparedStatement insertMedicineStmt;
    private final PreparedStatement insertDeviceStmt;
    private final PreparedStatement insertIngredientStmt;
    private final PreparedStatement insertPriceStmt;
    private final PreparedStatement findAllStmt;
    private final PreparedStatement loadIngrStmt;
    private final PreparedStatement loadPriceStmt;
    private final PreparedStatement updateProductStmt;
    private final PreparedStatement deleteIngrStmt;
    private final PreparedStatement deletePriceStmt;
    private final PreparedStatement deleteMedStmt;
    private final PreparedStatement deleteDevStmt;
    private final PreparedStatement deleteProdStmt;

    public ProductRepository() {
        try {
            this.connection = DBConnection.getConnection();

            this.insertProductStmt = connection.prepareStatement(INSERT_PRODUCT);
            this.insertMedicineStmt = connection.prepareStatement(INSERT_MEDICINE);
            this.insertDeviceStmt = connection.prepareStatement(INSERT_DEVICE);
            this.insertIngredientStmt = connection.prepareStatement(INSERT_INGREDIENT);
            this.insertPriceStmt = connection.prepareStatement(INSERT_PRICE);
            this.findAllStmt = connection.prepareStatement(FIND_ALL);
            this.loadIngrStmt = connection.prepareStatement(LOAD_INGR);
            this.loadPriceStmt = connection.prepareStatement(LOAD_PRICE);
            this.updateProductStmt = connection.prepareStatement(UPDATE_PRODUCT);
            this.deleteIngrStmt = connection.prepareStatement(DELETE_INGR);
            this.deletePriceStmt = connection.prepareStatement(DELETE_PRICE);
            this.deleteMedStmt = connection.prepareStatement(DELETE_MED);
            this.deleteDevStmt = connection.prepareStatement(DELETE_DEV);
            this.deleteProdStmt = connection.prepareStatement(DELETE_PROD);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize ProductRepository statements", e);
        }
    }

    public void create(Product p) {
        try {
            connection.setAutoCommit(false);

            insertProductStmt.setInt(1, p.getProductId());
            insertProductStmt.setInt(2, p.getManufacturerId());
            insertProductStmt.setString(3, p.getName());
            insertProductStmt.setString(4, p.getForm());
            insertProductStmt.setString(5, p.getGtin());
            insertProductStmt.executeUpdate();

            if (p.getType() == ProductType.MEDICINE) {
                insertMedicineStmt.setInt(1, p.getProductId());
                insertMedicineStmt.setBoolean(2, p.getRxFlag());
                insertMedicineStmt.executeUpdate();
            } else {
                insertDeviceStmt.setInt(1, p.getProductId());
                insertDeviceStmt.setString(2, p.getRiskClass());
                insertDeviceStmt.executeUpdate();
            }

            for (IngredientAmount ia : p.getIngredients()) {
                insertIngredientStmt.setInt(1, p.getProductId());
                insertIngredientStmt.setInt(2, ia.getIngredientId());
                insertIngredientStmt.setDouble(3, ia.getAmount());
                insertIngredientStmt.setString(4, ia.getUnit());
                insertIngredientStmt.executeUpdate();
            }

            ProductPrice price = p.getCurrentPrice();
            if (price != null) {
                insertPriceStmt.setInt(1, price.getPriceId());
                insertPriceStmt.setInt(2, p.getProductId());
                insertPriceStmt.setDate(3, Date.valueOf(price.getValidFrom()));
                insertPriceStmt.setDate(4, price.getValidTo() == null ? null : Date.valueOf(price.getValidTo()));
                insertPriceStmt.setDouble(5, price.getPrice());
                insertPriceStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Product creation failed", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (ResultSet rs = findAllStmt.executeQuery()) {
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
        try {
            loadIngrStmt.setInt(1, productId);
            try (ResultSet rs = loadIngrStmt.executeQuery()) {
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

    public ProductPrice loadCurrentPrice(int productId) {
        try {
            loadPriceStmt.setInt(1, productId);
            try (ResultSet rs = loadPriceStmt.executeQuery()) {
                if (rs.next()) {
                    return new ProductPrice(
                            rs.getInt("price_id"),
                            productId,
                            rs.getDate("valid_from").toLocalDate(),
                            rs.getDate("valid_to") == null ? null : rs.getDate("valid_to").toLocalDate(),
                            rs.getDouble("price")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Product p) {
        try {
            updateProductStmt.setInt(1, p.getManufacturerId());
            updateProductStmt.setString(2, p.getName());
            updateProductStmt.setString(3, p.getForm());
            updateProductStmt.setString(4, p.getGtin());
            updateProductStmt.setInt(5, p.getProductId());
            updateProductStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public void delete(int productId) {
        try {
            connection.setAutoCommit(false);

            deleteIngrStmt.setInt(1, productId);
            deleteIngrStmt.executeUpdate();

            deletePriceStmt.setInt(1, productId);
            deletePriceStmt.executeUpdate();

            deleteMedStmt.setInt(1, productId);
            deleteMedStmt.executeUpdate();

            deleteDevStmt.setInt(1, productId);
            deleteDevStmt.executeUpdate();

            deleteProdStmt.setInt(1, productId);
            deleteProdStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void close() throws Exception {
        if (insertProductStmt != null) insertProductStmt.close();
        if (insertMedicineStmt != null) insertMedicineStmt.close();
        if (insertDeviceStmt != null) insertDeviceStmt.close();
        if (insertIngredientStmt != null) insertIngredientStmt.close();
        if (insertPriceStmt != null) insertPriceStmt.close();
        if (findAllStmt != null) findAllStmt.close();
        if (loadIngrStmt != null) loadIngrStmt.close();
        if (loadPriceStmt != null) loadPriceStmt.close();
        if (updateProductStmt != null) updateProductStmt.close();
        if (deleteIngrStmt != null) deleteIngrStmt.close();
        if (deletePriceStmt != null) deletePriceStmt.close();
        if (deleteMedStmt != null) deleteMedStmt.close();
        if (deleteDevStmt != null) deleteDevStmt.close();
        if (deleteProdStmt != null) deleteProdStmt.close();
        if (connection != null) connection.close();
    }
}