package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.IO.Workspace.WorkspaceWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Timer;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
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
import org.controlsfx.control.textfield.CustomTextField;

public class Controller implements Initializable {
    //TODO: change soulless from item to npc
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
    private JFXCheckBox check_shift, check_render;

    @FXML
    private TextField text_name, text_vshift, text_hshift, text_boundw, text_boundh, text_type;

   // @FXML
   // private CustomTextField text_search;

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

    @FXML
    private ChoiceBox choice_basic_head, choice_basic_body, choice_basic_legs, choice_type, choice_layer;

    private Timer playTimer = new Timer();
    private TimerTask playTask;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Entry.TYPE type : Entry.TYPE.values()) {
            choice_type.getItems().add(type);
        }

        choice_type.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                choice_layer.getItems().clear();
                choice_layer.getItems().addAll(((Entry.TYPE)t1).getLayers());
            }
        });

        choice_basic_head.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_basic_body.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_basic_legs.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
//        text_search.setLeft(GlyphsDude.createIcon(FontAwesomeIcon.SEARCH, "15px"));
//        text_search.setRight(GlyphsDude.createIcon(FontAwesomeIcon.CLOSE, "15px"));
//        text_search.getRight().setVisible(false);
//        text_search.getRight().setOnMouseClicked(e -> {
//            if (text_search.getRight().isVisible()) {
//                text_search.setText("");
//            }
//        });
//        text_search.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                if (t1.isEmpty()) {
//                    text_search.getRight().setVisible(false);
//                    text_search.getRight().setCursor(Cursor.DEFAULT);
//                } else {
//                    text_search.getRight().setVisible(true);
//                    text_search.getRight().setCursor(Cursor.HAND);
//
//                }
//
//            }
//        });

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
            if (needSave((Subspace)list_subspaces.getSelectionModel().getSelectedItem())) {
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
            choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fhead1"));
            choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fbody1"));
            choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
        });
        button_male.setOnMouseClicked(e -> {
            choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("head1"));
            choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("body1"));
            choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
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
                popup.show(spriteTool.getPrimaryStage(),event.getSceneX(), event.getSceneY(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
        });

        list_subspaces.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Subspace>() {
            @Override
            public void changed(ObservableValue<? extends Subspace> observableValue, Subspace oldSubspace, Subspace newSubspace) {
                if (newSubspace == null ||
                    !triggerListeners)
                    return;

                if (needSave(oldSubspace)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    alert.showAndWait();
                    if (alert.getResult() != ButtonType.OK) {
                        triggerListeners = false;
                        list_subspaces.getSelectionModel().select(oldSubspace);
                        triggerListeners = true;
                        return;
                    }
                }

                spriteTool.clearWorkingCopy();
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
                popup.show(spriteTool.getPrimaryStage(),event.getSceneX(), event.getSceneY(),JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT,0,0);
        });

        list_entries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observableValue, Entry oldEntry, Entry newEntry) {
                if (newEntry == null)
                    return;

                if (needSave((Subspace)list_subspaces.getSelectionModel().getSelectedItem())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    alert.showAndWait();
                    if (alert.getResult() != ButtonType.OK) {
                        list_entries.getSelectionModel().select(oldEntry);
                        return;
                    }
                }
                spriteTool.getSpriteRenderer().reset();
                spriteTool.setWorkingCopy((Subspace)list_subspaces.getSelectionModel().getSelectedItem(),newEntry.clone());
                checkSave();
                showEntry(spriteTool.getWorkingCopy());
            }
        });
        color_bluescale.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                render();
            }
        });
        color_bluescale.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        color_grayscale.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                render();
            }
        });
        color_grayscale.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        canvas.setOnScroll(e -> {
            if (e.isControlDown()) {
                if (scroll_canvas.isDisable())
                    return;
                double newValue = scroll_canvas.getValue() + Math.signum(e.getDeltaY());
                if (newValue > scroll_canvas.getMax())
                    newValue = scroll_canvas.getMax();
                else if (newValue < scroll_canvas.getMin())
                    newValue = scroll_canvas.getMin();

                scroll_canvas.setValue(newValue);
            } else {
                if (scroll_zoom.isDisable())
                    return;
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
                //spriteTool.getSpriteRenderer().renderPlayer(t1.intValue()-1);

                if (spriteTool.getWorkingCopy() == null)
                    return;

                showEntry(spriteTool.getWorkingCopy(), t1.intValue()-1);
            }
        });
        //------- Checkbox
        check_render.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1 == null)
                    return;

                render();
            }
        });
        check_shift.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        check_shift.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null)
                    return;

                getWorkingSprite().getInfo().setUseShift(t1);
                checkSave();

                render();
            }
        });
        //------- Textboxes
        text_name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty())
                    return;

                for (Sprite frame : spriteTool.getWorkingCopy().getFrames()) {
                    frame.getInfo().setID(t1);
                }

                spriteTool.getWorkingCopy().setID(t1);
                checkSave();
            }
        });
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

                int value = Integer.parseInt(t1);
                if (value > canvas.getFitWidth()-2 ||
                        value <= 0)
                    return;

                getWorkingSprite().getInfo().setBoundHeight(value);

                checkSave();
                render();
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

                int value = Integer.parseInt(t1);
                if (value > canvas.getFitWidth()-2 ||
                    value <= 0)
                    return;

                getWorkingSprite().getInfo().setBoundWidth(value);
                checkSave();

                render();
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

                getWorkingSprite().getInfo().setOffsetX(Integer.parseInt(t1));
                checkSave();

                render();
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

                getWorkingSprite().getInfo().setOffsetY(Integer.parseInt(t1));
                checkSave();
            }
        });
        text_name.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
    }

    //------------------ private methods
    private void populateEntryList(Subspace ss) {
//        this.l_entries.getItems().clear();
//        for (Entry entry : ss.getEntryList()) {
//            this.l_entries.getItems().add(entry);
//        }
        FilteredList<Entry> filteredEntryList = new FilteredList<>(ss.getEntryList(), s -> true);
//        text_search.textProperty().addListener(obs-> {
//            String filter = text_search.getText();
//            if (filter == null || filter.isEmpty()) {
//                filteredEntryList.setPredicate(s -> true);
//            } else
//                filteredEntryList.setPredicate(s -> s.toString().contains(filter));
//
//            if (filteredEntryList.size() == 0)
//                text_search.getStyleClass().add("textField-red");
//            else
//                text_search.getStyleClass().removeAll("textField-red");
//        });
        list_entries.setItems(filteredEntryList);
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
        JFXButton btn_cloneCategory = new JFXButton("Clone Entry");
        buttons.add(btn_newCategory);

        if (list_entries.getSelectionModel().getSelectedItem() != null)
            buttons.add(btn_cloneCategory);
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

    private void showEntry(Entry entry) {
        triggerListeners = false;
        scroll_canvas.setValue(1);
        scroll_canvas.setMax(entry.frameCount());
        scroll_canvas.setDisable(false);
        scroll_zoom.setValue(0);
        showEntry(entry, 0);
    }

    private void showEntry(Entry newEntry, int frame) {
        spriteTool.getSpriteRenderer().clear();
        spriteTool.getSpriteRenderer().wipeBuffer();
        Sprite sprite = newEntry.getFrame(frame);

        if (sprite == null)
            return;

        triggerListeners = false;
        Info info = sprite.getInfo();
        text_name.setText(newEntry.getID());
        check_shift.setSelected(info.getUseShift());
        text_hshift.setText(String.valueOf(info.getOffsetX()));
        text_vshift.setText(String.valueOf(info.getOffsetX()));
        text_boundh.setText(String.valueOf(info.getBoundHeight()));
        text_boundw.setText(String.valueOf(info.getBoundWidth()));
        label_frame.setText(info.getFrame() + " / " + info.getFrameCount());
        triggerListeners = true;

        render();
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
        if (needSave((Subspace)list_subspaces.getSelectionModel().getSelectedItem()))
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

    public boolean needSave(Subspace ss) {
        if (spriteTool.getWorkingCopy() == null)
            return false;

        if (spriteTool.getWorkingCopyIndex() == -1)
            return false;

        Entry savedEntry = ss.getEntryList().get(spriteTool.getWorkingCopyIndex());

        if (savedEntry == null)
            return false;

        if (!spriteTool.getWorkingCopy().equals(savedEntry))
            return true;

        return false;
    }

    public void stopTimer() {
        if (playTask != null)
            playTask.cancel();

        playTimer.cancel();
    }

    public Sprite getWorkingSprite() {
        int index = (int)scroll_canvas.getValue()-1;
        if (spriteTool.getWorkingCopy() == null)
            return null;

        return spriteTool.getWorkingCopy().getFrame(index);
    }

    public void render() {
        int frame = (int)scroll_canvas.getValue()-1;
        spriteTool.getSpriteRenderer().wipeBuffer();
        spriteTool.getSpriteRenderer().clear();
        if (check_render.selectedProperty().getValue()) {
            //TODO: add color support
            spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[getWorkingSprite().getInfo().getLayer().getIndex()] = spriteTool.getWorkingCopy();
            spriteTool.getSpriteRenderer().renderPlayer(frame);
        }
        else
            spriteTool.getSpriteRenderer().renderSprite(getWorkingSprite(), color_grayscale.getValue(), color_bluescale.getValue());
    }

    public void loadChoiceBoxes() {
        choice_basic_head.getItems().clear();

        //Load from the baked-in animations
        Subspace shippedAnimations = spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations();
        for (Entry entry : shippedAnimations.getEntryList()) {
            switch (entry.getType()) {
                case PLAYER_PART:
                    switch (entry.getLayer()) {
                        case HEAD_NO_SKIN:
                            choice_basic_head.getItems().add(entry);
                            break;
                        case BODY_NO_SKIN:
                            choice_basic_body.getItems().add(entry);
                            break;
                        case LEGS_NO_SKIN:
                            choice_basic_legs.getItems().add(entry);
                            break;
                        default:
                            break;
                    }
                    break;
                case PLAYER_EQUIPPABLE_HASCOMBAT:
                    break;
                case PLAYER_EQUIPPABLE_NOCOMBAT:
                    break;
                default:
                    break;
            }
        }

        //Load from the current workspace
        if (spriteTool.getWorkspace() != null) {
            for (Subspace subspace : spriteTool.getWorkspace().getSubspaces()) {
                if (subspace != null) {
                    for (Entry entry : subspace.getEntryList()) {
                        switch (entry.getType()) {
                            case PLAYER_PART:
                                switch (entry.getLayer()) {
                                    case HEAD_NO_SKIN:
                                        if (!choice_basic_head.getItems().contains(entry))
                                            choice_basic_head.getItems().add(entry);
                                        break;
                                    case BODY_NO_SKIN:
                                        if (!choice_basic_body.getItems().contains(entry))
                                            choice_basic_body.getItems().add(entry);
                                        break;
                                    case LEGS_NO_SKIN:
                                        if (!choice_basic_legs.getItems().contains(entry))
                                            choice_basic_legs.getItems().add(entry);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case PLAYER_EQUIPPABLE_HASCOMBAT:
                                break;
                            case PLAYER_EQUIPPABLE_NOCOMBAT:
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
}
