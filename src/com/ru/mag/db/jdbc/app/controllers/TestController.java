package com.ru.mag.db.jdbc.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestController {
    @FXML
    Label testLabel;

    public void setLabelText(String text){
        testLabel.setText(text);
    }
    public void closeDialog(ActionEvent event){
        ((Stage)(((Node)event.getSource()).getScene().getWindow())).close();
    }
}
