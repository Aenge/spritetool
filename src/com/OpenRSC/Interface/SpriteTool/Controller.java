package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.SpriteTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private ScrollBar scroll_zoom;

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
                popup.show(spriteTool.getPrimaryStage(),event.getX() + l_subspaces.layoutXProperty().intValue(), event.getY() + l_subspaces.layoutYProperty().intValue() - popup.getPopupContent().getPrefHeight(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
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
            double newValue = scroll_zoom.getValue() - 4*Math.signum(e.getDeltaY());
            if (newValue > scroll_zoom.getMax())
                newValue = scroll_zoom.getMax();
            else if (newValue < scroll_zoom.getMin())
                newValue = scroll_zoom.getMin();
            scroll_zoom.setValue(newValue);
        });

        scroll_zoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (t1.intValue() == number.intValue())
                    return;
                Rectangle2D viewportRect = new Rectangle2D(-t1.intValue(), -t1.intValue(), 300+2*t1.intValue(), 300+2*t1.intValue());
                canvas.setViewport(viewportRect);
            }
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
        /*
        Entry entry = (Entry)l_entries.getSelectionModel().getSelectedItem();
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File("C://temp/bargain.json"), entry.getSpriteRep().getInfo());
        } catch (IOException a) {
            a.printStackTrace();
        }
*/
        ObjectMapper om = new ObjectMapper();
        Info info;
        try {

            info = om.readValue(new File("C://temp/bargain.json"), Info.class);
        } catch (IOException a) {
            a.printStackTrace();
        }

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
        this.l_subspaces.setItems(ws.getSubspaces());
    }

    private JFXPopup buildSubspaceMenu() {
        if (spriteTool.getWorkspace() == null)
            return null;
        JFXPopup popup = new JFXPopup();
        List<JFXButton> buttons = new ArrayList<>();
        JFXButton btn_newCategory = new JFXButton("New Category");
        btn_newCategory.setOnMouseClicked(e -> {
            popup.hide();
            TextInputDialog td = new TextInputDialog();
            td.setHeaderText("Enter the name of the new category");
            td.showAndWait();
            if (td.getEditor().getText().isEmpty())
                return;

            spriteTool.getWorkspace().createSubspace(td.getEditor().getText());
        });
        btn_newCategory.setPadding(new Insets(10));
        buttons.add(btn_newCategory);
        Subspace ss;
        if ((ss = (Subspace)l_subspaces.getSelectionModel().getSelectedItem()) != null) {
            JFXButton btn_delCategory = new JFXButton("Delete " + ss.getName());
            JFXButton btn_renameCategory = new JFXButton("Rename " + ss.getName());

            btn_delCategory.setOnMouseClicked(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setHeaderText("Are you sure you want to remove category " + ss.getName() + "? This action can't be undone.");
                confirm.showAndWait();
                if (confirm.getResult().getButtonData().isDefaultButton()) {
                    if (!spriteTool.getWorkspace().deleteSubspace(ss)) {
                        showError("Could not delete subspace.");
                    }
                }

            });

            btn_renameCategory.setOnMouseClicked(e -> {

                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Please enter the new name for category " + ss.getName());
                td.showAndWait();
                File path = ss.getPath().toFile();
                File newPath = new File(path.getParent().toString(), td.getEditor().getText());

                if (newPath.exists()) {
                    showError("That category already exists.");
                    return;
                }

                if (!path.renameTo(newPath)) {
                    showError("Error renaming category.");
                    return;
                }

                ss.setPath(newPath.toPath());
            });
            buttons.add(btn_delCategory);
            buttons.add(btn_renameCategory);
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttons);

        int height = 0;
        for (JFXButton button : buttons) {
            height += button.getMaxHeight();
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPadding(new Insets(10));
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setOnMouseEntered(e -> button.requestFocus());
        }

        vbox.setPrefHeight(height);

        popup.setPopupContent(vbox);
        return popup;
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
        scroll_zoom.setValue(0);
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

    public void showError(String text) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText(text);
        error.showAndWait();
    }
}
