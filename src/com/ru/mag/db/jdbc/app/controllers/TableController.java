package com.ru.mag.db.jdbc.app.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableController {

    @FXML
    TableView tableView1;

    @FXML
    Label testLabel;

    public void setTableResultset(ResultSet resultSet, String labelText) throws SQLException {

        testLabel.setText(labelText);

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {

            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                return new SimpleStringProperty(param.getValue().get(j).toString());
            });

            tableView1.getColumns().add(col);
        }

        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            data.add(row);

        }

        tableView1.setItems(data);
    }

}
