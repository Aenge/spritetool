package com.SpriteTool.PopMenu;
import com.SpriteTool.SpriteTool;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private SpriteTool spriteTool;

    @FXML
    private JFXButton b_OpenWorkspace;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //----- BUTTON OPEN WORKSPACE
        b_OpenWorkspace.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.FOLDER_OPEN, "30px"));
        b_OpenWorkspace.setContentDisplay(ContentDisplay.TOP);
        b_OpenWorkspace.setText("Open Workspace");
    }

    public void b_OpenWorkSpace_Click() {
        spriteTool.loadWorkspace();
    }

    public void setSpriteTool(SpriteTool spritetool) {
        this.spriteTool = spritetool;
    }
}