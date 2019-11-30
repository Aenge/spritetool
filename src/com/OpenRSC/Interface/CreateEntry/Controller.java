package com.OpenRSC.Interface.CreateEntry;

import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private SpriteTool spriteTool;

    @FXML
    private JFXButton button_select;

    @FXML
    private ImageView image_preview;

    @FXML
    private TextField text_name, text_image;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        button_select.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FOLDER_OPEN));
        button_select.getStyleClass().add("glyph-icon");
    }

    public void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
    }
}
