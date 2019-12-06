package com.OpenRSC.Interface.CreateEntry;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Render.PlayerRenderer;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private SpriteTool spriteTool;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton button_accept, button_cancel;

    @FXML
    private TextField text_name;

    @FXML
    private ChoiceBox choice_type, choice_slot;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        button_accept.disableProperty().bind(Bindings.or(text_name.textProperty().isEmpty(), choice_type.valueProperty().isNull()));
        button_accept.setOnMouseClicked(e -> {
            Subspace subspace = spriteTool.getMainController().getCurrentSubspace();
            if (subspace.addNewEntry(text_name.getText(), (Entry.TYPE)choice_type.getValue(), (PlayerRenderer.LAYER)choice_slot.getValue())) {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

        button_cancel.setOnMouseClicked(e -> {
            ((Stage)root.getScene().getWindow()).close();
        });

        choice_type.getItems().addAll(Entry.TYPE.values());
        choice_slot.getItems().addAll(PlayerRenderer.LAYER.values());
        choice_type.valueProperty().addListener(new ChangeListener<Entry.TYPE>() {
            @Override
            public void changed(ObservableValue observableValue, Entry.TYPE o, Entry.TYPE t1) {
               if (t1.getLayers().length > 0) {
                   choice_slot.getItems().setAll(t1.getLayers());
                   choice_slot.setDisable(false);
               } else {
                   choice_slot.getItems().clear();
                   choice_slot.setDisable(true);
               }
            }
        });
        choice_slot.setDisable(true);
    }

    public void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
    }
}
