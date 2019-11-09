package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Sprite;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


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
    private TextField text_boundw;

    @FXML
    private TextField text_boundh;

    @FXML
    private ImageView canvas;

    @FXML
    private ScrollBar scroll_canvas;

    int changer = 0;
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
                scroll_canvas.setDisable(true);
                scroll_canvas.setMax(1);
                scroll_canvas.setValue(1);
                spriteTool.getSpriteRenderer().clear();
                populateEntryList(newSubspace);
            }
        });
        l_entries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observableValue, Entry oldEntry, Entry newEntry) {
                if (newEntry == null)
                    return;
                showEntry(newEntry);
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

        canvas.setOnScroll(e -> {
            changer += 2*Math.signum(e.getDeltaY());
            if (changer > 149)
                changer = 149;
            Rectangle2D viewportRect = new Rectangle2D(changer, changer, 300-2*changer, 300-2*changer);
            canvas.setViewport(viewportRect);
        });

        scroll_canvas.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scroll_canvas.setValue(t1.intValue());
                if (scroll_canvas.getValue() == number.intValue())
                    return;

                Entry entry = (Entry)l_entries.getSelectionModel().getSelectedItem();
                if (entry == null)
                    return;

                Sprite sprite = entry.getAnimation().getFrame(t1.intValue());
                int xOffset = (spriteTool.getSpriteRenderer().getWidth2() - sprite.getImageData().getWidth())/2;
                int yOffset = (spriteTool.getSpriteRenderer().getHeight2() - sprite.getImageData().getHeight())/2;
                spriteTool.getSpriteRenderer().clear();
                spriteTool.getSpriteRenderer().bufferSprite(sprite, xOffset, yOffset,sprite.getImageData().getWidth(), sprite.getImageData().getHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
                //should be bound widths like this
                // spriteRenderer.bufferSprite(newEntry.getSpriteRep(), 0, 0, newEntry.getSpriteRep().getInfo().getBoundWidth(), newEntry.getSpriteRep().getInfo().getBoundHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
                spriteTool.getSpriteRenderer().render();
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

    private void showEntry(Entry newEntry) {
        int xOffset = (spriteTool.getSpriteRenderer().getWidth2() - newEntry.getSpriteRep().getImageData().getWidth())/2;
        int yOffset = (spriteTool.getSpriteRenderer().getHeight2() - newEntry.getSpriteRep().getImageData().getHeight())/2;
        spriteTool.getSpriteRenderer().clear();
        spriteTool.getSpriteRenderer().bufferSprite(newEntry.getSpriteRep(), xOffset, yOffset, newEntry.getSpriteRep().getImageData().getWidth(), newEntry.getSpriteRep().getImageData().getHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
        //should be bound widths like this
        // spriteRenderer.bufferSprite(newEntry.getSpriteRep(), 0, 0, newEntry.getSpriteRep().getInfo().getBoundWidth(), newEntry.getSpriteRep().getInfo().getBoundHeight(), 0, 0, 0, false, 0, 1, 0xFFFFFFFF);
        spriteTool.getSpriteRenderer().render();

        text_name.setText(newEntry.getName());
        check_shift.setSelected(newEntry.getSpriteRep().getInfo().getUseShift());
        text_hshift.setText(newEntry.getSpriteRep().getInfo().getOffsetX() + "");
        text_vshift.setText(newEntry.getSpriteRep().getInfo().getOffsetX() + "");
        text_boundh.setText(newEntry.getSpriteRep().getInfo().getBoundHeight() + "");
        text_boundw.setText(newEntry.getSpriteRep().getInfo().getBoundWidth() + "");

        if (newEntry.isAnimation() && newEntry.getAnimation().getFrameCount() > 1) {
            scroll_canvas.setMax(newEntry.getAnimation().getFrameCount());
            scroll_canvas.setDisable(false);
        }
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

    public ImageView getCanvas() { return this.canvas; }
}
