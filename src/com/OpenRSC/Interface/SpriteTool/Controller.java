package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.IO.Workspace.WorkspaceWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Animation;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Timer;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
public class Controller implements Initializable {

    private SpriteTool spriteTool;
    private boolean triggerListeners = true;

    @FXML
    private VBox root;

    @FXML
    private HBox hbox_menu;

    @FXML
    private AnchorPane pane_animation;

    @FXML
    private JFXListView list_subspaces, list_entries;

    @FXML
    private Label label_status, label_frame, label_framecount;

    @FXML
    private JFXCheckBox check_shift;

    @FXML
    private TextField text_name, text_vshift, text_hshift, text_boundw, text_boundh,text_type;

    @FXML
    private ImageView canvas;

    @FXML
    private ScrollBar scroll_canvas, scroll_zoom;

    @FXML
    private JFXButton button_new_workspace, button_open_workspace, button_save_workspace, button_addframe, button_changepng;

    @FXML
    private ToggleButton button_play, button_male, button_female;

    @FXML
    private ColorPicker color_grayscale, color_bluescale;

    @FXML
    public ProgressBar progress_bar;

    private Timer playTimer = new Timer();
    private TimerTask playTask;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hbox_menu.setOnMouseExited(e->{
            root.requestFocus();
        });

        //--------- Menu buttons
        button_new_workspace.setOnMouseEntered(e -> button_new_workspace.requestFocus());
        button_open_workspace.setOnMouseEntered(e -> button_open_workspace.requestFocus());
        button_save_workspace.setOnMouseEntered(e -> button_save_workspace.requestFocus());
        button_new_workspace.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.PLUS, "23px"));
        button_new_workspace.setOnMouseClicked(e -> {
            spriteTool.createWorkspace();
        });
        button_open_workspace.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.FOLDER_OPEN_ALT, "23px"));
        button_open_workspace.setOnMouseClicked(e -> {
            if (needSave()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Loading a new workspace will cause you to lose your unsaved changes.");
                alert.showAndWait();
                if (alert.getResult() != ButtonType.OK)
                    return;
            }
            spriteTool.openWorkspace();
        });
        button_save_workspace.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.SAVE, "23px"));
        button_save_workspace.setOnMouseClicked(e -> {
            Subspace ss = (Subspace)list_subspaces.getSelectionModel().getSelectedItem();
            Entry entry = (Entry)list_entries.getSelectionModel().getSelectedItem();

            if (ss == null || entry == null)
                return;

            WorkspaceWriter wsWriter = new WorkspaceWriter(spriteTool.getWorkspace());

            if (wsWriter.updateEntry(ss, entry, spriteTool.getWorkingCopy())) {
                int index = ss.getEntryList().indexOf(entry);
                if (index > -1) {
                    ss.getEntryList().set(index, spriteTool.getWorkingCopy());
                }
            } else
                showError("There was a problem saving your changes.");

            checkSave();
        });

        //--------- Other Buttons
        button_changepng.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.EDIT, "15px"));
        button_changepng.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_female.setOnMouseClicked(e -> {
            //spriteTool.getSpriteRenderer().renderPlayer();
        });
        button_addframe.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.PLUS, "15px"));
        button_addframe.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_play.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.PLAY, "15px"));
        button_play.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_play.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    button_play.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.STOP, "15px"));
                    button_play.getStyleClass().add("red-icon");
                    button_play.setText("stop");
                    playAnimation();
                } else {
                    button_play.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.PLAY, "15px"));
                    button_play.getStyleClass().remove("red-icon");
                    button_play.setText("play");
                    stopAnimation();
                }
            }
        });
        button_male.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.MALE,"15px"));
        button_male.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1)
                    button_female.setSelected(false);
                root.requestFocus();
            }
        });
        button_female.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.FEMALE,"15px"));
        button_female.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1)
                    button_male.setSelected(false);
                root.requestFocus();
            }
        });

        //--------- subspace list
        list_subspaces.setOnContextMenuRequested(event -> {
            JFXPopup popup = buildSubspaceMenu();
            if (popup != null)
                popup.show(spriteTool.getPrimaryStage(),event.getX() + list_subspaces.layoutXProperty().intValue(), event.getY() + list_subspaces.layoutYProperty().intValue() - popup.getPopupContent().getPrefHeight(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
        });

        list_subspaces.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Subspace>() {
            @Override
            public void changed(ObservableValue<? extends Subspace> observableValue, Subspace oldSubspace, Subspace newSubspace) {
                if (newSubspace == null ||
                    !triggerListeners)
                    return;

                if (needSave()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    alert.showAndWait();
                    if (alert.getResult() != ButtonType.OK) {
                        triggerListeners = false;
                        list_subspaces.getSelectionModel().select(oldSubspace);
                        triggerListeners = true;
                        return;
                    }
                }

                spriteTool.setWorkingCopy(null);
                scroll_canvas.setDisable(true);
                scroll_canvas.setMax(1);
                scroll_canvas.setValue(1);
                scroll_zoom.setValue(0);
                spriteTool.getSpriteRenderer().reset();
                populateEntryList(newSubspace);
                checkSave();
            }
        });

        //-------- Entries list
        list_entries.setOnContextMenuRequested(event -> {
            JFXPopup popup = buildEntryMenu();
            if (popup != null)
                popup.show(spriteTool.getPrimaryStage(),event.getX() + list_entries.layoutXProperty().intValue(), event.getY() + list_entries.layoutYProperty().intValue() - popup.getPopupContent().getPrefHeight(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
        });

        list_entries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observableValue, Entry oldEntry, Entry newEntry) {
                if (newEntry == null)
                    return;

                if (needSave(oldEntry)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    alert.showAndWait();
                    if (alert.getResult() != ButtonType.OK) {
                        list_entries.getSelectionModel().select(oldEntry);
                        return;
                    }
                }
                spriteTool.getSpriteRenderer().reset();
                spriteTool.setWorkingCopy(newEntry.clone());
                checkSave();
                showEntry(spriteTool.getWorkingCopy());
            }
        });
        color_bluescale.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                spriteTool.getSpriteRenderer().renderSprite(spriteTool.getWorkingCopy().getSprite(), color_grayscale.getValue(), t1);
            }
        });
        color_bluescale.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        color_grayscale.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                spriteTool.getSpriteRenderer().renderSprite(spriteTool.getWorkingCopy().getSprite(), t1, color_bluescale.getValue());
            }
        });
        color_grayscale.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        canvas.setOnScroll(e -> {
            if (e.isControlDown()) {
                double newValue = scroll_canvas.getValue() + Math.signum(e.getDeltaY());
                if (newValue > scroll_canvas.getMax())
                    newValue = scroll_canvas.getMax();
                else if (newValue < scroll_canvas.getMin())
                    newValue = scroll_canvas.getMin();

                scroll_canvas.setValue(newValue);
            } else {
                double newValue = scroll_zoom.getValue() - 4*Math.signum(e.getDeltaY());
                if (newValue > scroll_zoom.getMax())
                    newValue = scroll_zoom.getMax();
                else if (newValue < scroll_zoom.getMin())
                    newValue = scroll_zoom.getMin();
                scroll_zoom.setValue(newValue);
            }

        });

        final Light.Point point = new Light.Point();
        canvas.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1)
                scroll_zoom.setValue(0);
        });
        canvas.setCursor(Cursor.HAND);

        canvas.setOnMousePressed(e -> {
            point.setX(e.getSceneX());
            point.setY(e.getSceneY());
            canvas.setCursor(Cursor.CLOSED_HAND);
        });

        canvas.setOnMouseReleased(e -> {
            canvas.setCursor(Cursor.HAND);
        });
        canvas.setOnMouseExited(e -> {
            canvas.setCursor(Cursor.HAND);
        });
        canvas.setOnMouseDragged(e -> {
            if (spriteTool.getWorkingCopy() == null)
                return;
            Rectangle2D viewPort = canvas.getViewport();
            double deltaX = (e.getSceneX()-point.getX()) * viewPort.getWidth() / canvas.getFitWidth();
            double deltaY = (e.getSceneY()-point.getY()) * viewPort.getHeight() / canvas.getFitHeight();
            Rectangle2D newPort = new Rectangle2D(viewPort.getMinX() - deltaX, viewPort.getMinY() - deltaY, viewPort.getWidth(), viewPort.getHeight());
            canvas.setViewport(newPort);
            point.setX(e.getSceneX());
            point.setY(e.getSceneY());
        });
        scroll_zoom.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
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

                if (spriteTool.getWorkingCopy() == null)
                    return;

                ((Animation)spriteTool.getWorkingCopy().getSpriteData()).frameProperty().setValue(t1);
                showEntry(spriteTool.getWorkingCopy());
            }
        });
        //------- Checkbox
        check_shift.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        check_shift.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null)
                    return;

                spriteTool.getWorkingCopy().getSprite().getInfo().setUseShift(t1);
                checkSave();

                spriteTool.getSpriteRenderer().renderSprite(spriteTool.getWorkingCopy().getSprite(),color_grayscale.getValue(), color_bluescale.getValue());
            }
        });
        //------- Textboxes
        text_boundh.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        text_boundh.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty())
                    return;

                if (!t1.matches("\\d*")) {
                    text_boundh.setText(s);
                    return;
                }

                Sprite sprite = spriteTool.getWorkingCopy().getSprite();

                if (Integer.parseInt(t1) < sprite.getImageData().getHeight())
                    return;

                sprite.getInfo().setBoundHeight(Integer.parseInt(t1));

                checkSave();
                spriteTool.getSpriteRenderer().renderSprite(sprite,color_grayscale.getValue(), color_bluescale.getValue());
            }
        });
        text_boundw.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        text_boundw.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty())
                    return;

                if (!t1.matches("\\d*")) {
                    text_boundh.setText(s);
                    return;
                }

                Sprite sprite = spriteTool.getWorkingCopy().getSprite();

                if (Integer.parseInt(t1) < sprite.getImageData().getWidth())
                    return;

                spriteTool.getWorkingCopy().getSprite().getInfo().setBoundWidth(Integer.parseInt(t1));
                checkSave();

                spriteTool.getSpriteRenderer().renderSprite(sprite,color_grayscale.getValue(), color_bluescale.getValue());
            }
        });
        text_hshift.disableProperty().bind(check_shift.selectedProperty().not());
        text_hshift.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty())
                    return;

                if (!t1.matches("\\d*")) {
                    text_boundh.setText(s);
                    return;
                }

                Sprite sprite = spriteTool.getWorkingCopy().getSprite();

                spriteTool.getWorkingCopy().getSprite().getInfo().setOffsetX(Integer.parseInt(t1));
                checkSave();

                spriteTool.getSpriteRenderer().renderSprite(sprite,color_grayscale.getValue(), color_bluescale.getValue());
            }
        });
        text_vshift.disableProperty().bind(check_shift.selectedProperty().not());
        text_vshift.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty())
                    return;

                if (!t1.matches("\\d*")) {
                    text_boundh.setText(s);
                    return;
                }

                Sprite sprite = spriteTool.getWorkingCopy().getSprite();

                spriteTool.getWorkingCopy().getSprite().getInfo().setOffsetY(Integer.parseInt(t1));
                checkSave();

                spriteTool.getSpriteRenderer().renderSprite(sprite,color_grayscale.getValue(), color_bluescale.getValue());
            }
        });
        text_name.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());

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
//        this.l_entries.getItems().clear();
//        for (Entry entry : ss.getEntryList()) {
//            this.l_entries.getItems().add(entry);
//        }
        list_entries.setItems(ss.getEntryList());
    }

    public void populateSubspaceList(Workspace ws) {
        this.list_subspaces.getItems().clear();
        this.list_subspaces.setItems(ws.getSubspaces());
    }

    private JFXPopup buildEntryMenu() {
        if (spriteTool.getWorkspace() == null
        || list_subspaces.getSelectionModel().getSelectedItem() == null)
            return null;

        JFXPopup popup = new JFXPopup();
        List<JFXButton> buttons = new ArrayList<>();
        JFXButton btn_newCategory = new JFXButton("New Entry");
        buttons.add(btn_newCategory);

        int height = 0;
        for (JFXButton button : buttons) {
            height += button.getMaxHeight();
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPadding(new Insets(10));
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setOnMouseEntered(e -> button.requestFocus());
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttons);
        vbox.setPrefHeight(height);

        popup.setPopupContent(vbox);
        return popup;
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
        if ((ss = (Subspace)list_subspaces.getSelectionModel().getSelectedItem()) != null) {
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
                if (td.getEditor().getText().isEmpty())
                    return;
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
        Sprite sprite = newEntry.getSprite();
        spriteTool.getSpriteRenderer().renderSprite(sprite,color_grayscale.getValue(), color_bluescale.getValue());

        triggerListeners = false;
        Info info = newEntry.getSprite().getInfo();
        text_name.setText(newEntry.getID());
        check_shift.setSelected(info.getUseShift());
        text_hshift.setText(String.valueOf(info.getOffsetX()));
        text_vshift.setText(String.valueOf(info.getOffsetX()));
        text_boundh.setText(String.valueOf(info.getBoundHeight()));
        text_boundw.setText(String.valueOf(info.getBoundWidth()));
        label_frame.setText(info.getFrame() + " / " + info.getFrameCount());
        scroll_zoom.setValue(0);
        if (newEntry.isAnimation() && ((Animation)newEntry.getSpriteData()).getFrameCount() > 1) {
            scroll_canvas.setMax(((Animation)newEntry.getSpriteData()).getFrameCount());
            scroll_canvas.setDisable(false);
        }
        triggerListeners = true;
    }

    private void playAnimation() {
        playTask = new TimerTask() {
            public void run() {
                int curFrame = (int)scroll_canvas.getValue();
                if (curFrame == (int)scroll_canvas.getMax())
                    Platform.runLater(()->scroll_canvas.setValue(1));
                else
                    Platform.runLater(()->scroll_canvas.setValue(curFrame + 1));
            }
        };
        playTimer.schedule(playTask, 1000, 1000);
    }

    private void stopAnimation() {
        playTask.cancel();
    }

    private void checkSave() {
        if (needSave())
            button_save_workspace.getStyleClass().addAll("button-red", "edit-icon");
        else
            button_save_workspace.getStyleClass().removeAll("button-red", "edit-icon");
    }

    //------------------- public methods
    public void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
    }

    public void setStatus(String status) { label_status.setText(status); }

    public ImageView getCanvas() { return this.canvas; }

    public void showError(String text) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText(text);
        error.showAndWait();
    }

    public VBox getRoot() { return root; }

    public boolean needSave() { return needSave((Entry)list_entries.getSelectionModel().getSelectedItem());  }
    public boolean needSave(Entry entry) {
        if (entry == null
                || spriteTool.getWorkingCopy() == null)
            return false;

        return !spriteTool.getWorkingCopy().equals(entry);
    }

    public void stopTimer() {
        if (playTask != null)
            playTask.cancel();

        playTimer.cancel();
    }
}
