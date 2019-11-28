package com.OpenRSC.Interface.CreateWorkspace;

import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField text_directory;

    @FXML
    private TextField text_name;

    @FXML
    private Label label_workspace_path;

    @FXML
    private Label label_display;

    @FXML
    private JFXButton button_choose_directory;

    @FXML
    private JFXButton button_accept;

    @FXML
    private JFXButton button_cancel;

    @FXML
    private AnchorPane root;

    private SpriteTool spriteTool;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        text_name.disableProperty().bind(text_directory.textProperty().isEmpty());
        text_name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty())
                    label_workspace_path.setText(null);
                else {
                    label_workspace_path.setText(String.join(File.separator, text_directory.getText(), t1));
                }

            }
        });

        label_display.visibleProperty().bind(label_workspace_path.textProperty().isNotEmpty());

        button_choose_directory.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FOLDER_OPEN));
        button_choose_directory.setOnMouseClicked(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(root.getScene().getWindow());

            if (selectedDirectory == null)
                return;

            if (selectedDirectory.exists())
                text_directory.setText(selectedDirectory.toString());
        });

        button_accept.disableProperty().bind(label_workspace_path.textProperty().isEmpty());
        button_accept.setOnMouseClicked(e -> {
            File newDir = new File(label_workspace_path.getText());
            if (newDir.exists()) {
                showError("That directory already exists.");
                return;
            }
            if (!newDir.mkdir()) {
                showError("Error making directory.");
                return;
            }

            spriteTool.openWorkspace(newDir.toPath());
            root.getScene().getWindow().hide();
        });
    }

    public void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
    }

    public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

}
