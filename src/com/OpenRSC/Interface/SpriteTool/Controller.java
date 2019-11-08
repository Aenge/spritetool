package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.SpriteRenderer;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


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
    private Label label_status;

    @FXML
    private JFXCheckBox check_shift;

    @FXML
    private TextField text_name;

    @FXML
    private TextField text_vshift;

    @FXML
    private TextField text_hshift;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //--------- HAMBURGER
        HamburgerNextArrowBasicTransition transition = new HamburgerNextArrowBasicTransition(hamburger);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            drawer.toggle();
        });

        //--------- DRAWER
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
        l_subspaces.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Subspace>() {
            @Override
            public void changed(ObservableValue<? extends Subspace> observableValue, Subspace oldSubspace, Subspace newSubspace) {
                if (newSubspace == null)
                    return;
                canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(), canvas.getHeight());
                populateEntryList(newSubspace);
            }
        });
        l_entries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observableValue, Entry oldEntry, Entry newEntry) {
                if (newEntry == null)
                    return;
                SpriteRenderer spriteRenderer = new SpriteRenderer(canvas);
                spriteRenderer.bufferSprite(newEntry.getSprite(), 0, 0, newEntry.getSprite().getImageData().getWidth(), newEntry.getSprite().getImageData().getHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
                spriteRenderer.render();

                text_name.setText(newEntry.getName());
                check_shift.setSelected(newEntry.getSprite().getInfo().getUseShift());
                text_hshift.setText(newEntry.getSprite().getInfo().getOffsetX() + "");
                text_vshift.setText(newEntry.getSprite().getInfo().getOffsetX() + "");
                //should be bound widths like this
                // spriteRenderer.drawSpriteClipping(newEntry.getSprite(), 0, 0, newEntry.getSprite().getInfo().getBoundWidth(), newEntry.getSprite().getInfo().getBoundHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
            }
        });

        //-------- Checkbox
        check_shift.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean previous, Boolean current) {
                text_hshift.setDisable(!current);
                text_vshift.setDisable(!current);
            }
        });
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

    private void populateEntryList(Subspace ss) {
        this.l_entries.getItems().clear();
        for (Entry entry : ss.getEntryList()) {
            this.l_entries.getItems().add(entry);
        }
    }

    public void populateSubspaceList(Workspace ws) {
        this.l_subspaces.getItems().clear();
        for (Subspace subspace : ws.getSubspaces()) {
            this.l_subspaces.getItems().add(subspace);
        }
        this.l_subspaces.getSelectionModel().selectFirst();
    }

    private JFXPopup buildSubspaceMenu() {
        JFXPopup popup = new JFXPopup();
        return null;
    }

    //------------------- public methods
    public void setSpriteTool(SpriteTool spriteTool) {
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

    public void closeDrawer() { this.drawer.close(); }
    public void openDrawer() { this.drawer.open(); }

    public void setStatus(String status) { label_status.setText(status); }
}
