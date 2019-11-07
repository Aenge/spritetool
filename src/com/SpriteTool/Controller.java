package com.SpriteTool;
import com.SpriteTool.Model.Entry;
import com.SpriteTool.Model.Subspace;
import com.SpriteTool.Model.Workspace;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class Controller implements Initializable {

    private SpriteTool spriteTool;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView l_subspaces;

    @FXML
    private JFXListView l_entries;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //--------- HAMBURGER
        HamburgerNextArrowBasicTransition transition = new HamburgerNextArrowBasicTransition(hamburger);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            drawer.toggle();
        });

        //--------- DRAWER

        FXMLLoader menuLoader = new FXMLLoader();

        drawer.setOnDrawerClosing(event -> {
            closeHamburger(transition);

        });
        drawer.setOnDrawerOpening(event -> {
            openHamburger(transition);
        });

        //--------- subspace list
        l_subspaces.setOnContextMenuRequested(event -> {
            JFXPopup popup = buildSubspaceMenu();
            if (popup != null)
                popup.show(spriteTool.getPrimaryStage(),event.getX() + l_subspaces.getLayoutX(), event.getY() + l_subspaces.getLayoutY(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
        });
        l_subspaces.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {

                Subspace subspace = spriteTool.getWorkspace().getSubspaceByName(newString);
                if (subspace == null)
                    return;

                populateEntryList(subspace);
            }
        });

        //---------- entry list

    }

    //------------------ private methods
    private void openHamburger(HamburgerNextArrowBasicTransition transition) {
        transition.setRate(1.0);
        transition.play();
    }

    private void closeHamburger(HamburgerNextArrowBasicTransition transition) {
        transition.setRate(-1.0);
        transition.play();
    }

    //------------------- public methods
    void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
        drawer.setSidePane(spriteTool.getMenuRoot());
        /*
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        File newfile = new File("file:\\C:\\Users\\Scott\\Documents\\workspace\\Textures\\3239.png");
        if (newfile.exists())
            System.out.print("nice");
        Image image = new Image(newfile.getPath());
        gc.drawImage(image, 10, 10, 90, 90);
         */
    }

    void closeDrawer() { this.drawer.close(); }
    void openDrawer() { this.drawer.open(); }

    void populateSubspaceList(Workspace ws) {
        this.l_subspaces.getItems().clear();
        for (Subspace subspace : ws.getSubspaces()) {
            this.l_subspaces.getItems().add(subspace.getName());
        }
        this.l_subspaces.getSelectionModel().selectFirst();
    }

    private void populateEntryList(Subspace ss) {
        this.l_entries.getItems().clear();
        for (Entry entry : ss.getEntryList()) {
            this.l_entries.getItems().add(entry);
        }
    }

    private JFXPopup buildSubspaceMenu() {
        JFXPopup popup = new JFXPopup();
        return null;
    }
}
