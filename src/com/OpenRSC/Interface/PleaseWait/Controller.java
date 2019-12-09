package com.OpenRSC.Interface.PleaseWait;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private VBox root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void close() {
        root.getScene().getWindow().hide();
    }
}
