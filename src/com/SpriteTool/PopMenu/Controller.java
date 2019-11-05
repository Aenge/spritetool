package com.SpriteTool.PopMenu;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXButton btn1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn1.setText("no!");
    }
}
