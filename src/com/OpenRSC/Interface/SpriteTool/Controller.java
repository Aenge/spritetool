package com.OpenRSC.Interface.SpriteTool;
import com.OpenRSC.IO.Archive.Packer;
import com.OpenRSC.IO.Archive.Unpacker;
import com.OpenRSC.IO.Workspace.WorkspaceWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.PlayerRenderer;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Timer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;

public class Controller implements Initializable {
    //TODO: remove add picture to create entry, make it add a template depending on the type.
    //TODO: fixup render player. need a layer handler / add null & null handlers to all
    //TODO: unsaved changes -> search for another one -> select it -> press cancel -> error message. need select proper thing
    //TODO: switching use offset should adjust the bounding boxes (pfp example)
    //TODO: loading a new workspace should clear the current entries
    //TODO: audit create/update subspace/entry/sprite. make them consistent.
    //TODO: put entries into their own folders
    //TODO: remove filename in info
    //TODO: needsave doesn't verify that selectedindex != -1 -> create new entry, open workspace, bug.
    //TODO: fix the slot/layer connectors in create entry interface
    //TODO: file chooser default path set it to something good
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

   @FXML
   private CustomTextField text_search;

    @FXML
    private ImageView canvas;

    @FXML
    private ScrollBar scroll_canvas, scroll_zoom;

    @FXML
    private JFXButton button_new_workspace, button_open_workspace, button_save_workspace, button_addframe, button_changepng, button_male, button_female, button_unpack, button_pack;

    @FXML
    private ToggleButton button_play;

    @FXML
    private ColorPicker color_grayscale, color_bluescale;

    @FXML
    public ProgressBar progress_bar;

    @FXML
    private ChoiceBox choice_basic_head, choice_basic_body, choice_basic_legs, choice_type, choice_layer, choice_head, choice_body, choice_legs, choice_main, choice_sub, choice_glove, choice_boot, choice_neck, choice_cape;

    @FXML
    private JFXSlider slider_period;

    private Timer playTimer = new Timer();
    private TimerTask playTask;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        check_render.setCheckedColor(spriteTool.accentColor);
        check_shift.setCheckedColor(spriteTool.accentColor);

        choice_type.getItems().add(null);
        for (Entry.TYPE type : Entry.TYPE.values()) {
            choice_type.getItems().add(type);
        }

        choice_type.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                choice_layer.getItems().clear();

                if (t1 != null)
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
        choice_head.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (o != null)
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[((Entry)o).getLayer().getIndex()] = null;

                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    if (entry.getLayer() == PlayerRenderer.LAYER.HEAD_NO_SKIN)
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.HEAD_WITH_SKIN.getIndex()] = null;
                    else
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.HEAD_NO_SKIN.getIndex()] = (Entry)choice_basic_head.getValue();
                    render();
                } else
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[((Entry)o).getLayer().getIndex()] = null;
            }
        });
        choice_body.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (o != null)
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[((Entry)o).getLayer().getIndex()] = null;

                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    if (entry.getLayer() == PlayerRenderer.LAYER.BODY_NO_SKIN)
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.BODY_WITH_SKIN.getIndex()] = null;
                    else
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.BODY_NO_SKIN.getIndex()] = (Entry)choice_basic_body.getValue();
                } else
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[((Entry)o).getLayer().getIndex()] = null;

                render();
            }
        });
        choice_legs.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    if (entry.getLayer() == PlayerRenderer.LAYER.LEGS_NO_SKIN)
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.LEGS_WITH_SKIN.getIndex()] = null;
                    else
                        spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[PlayerRenderer.LAYER.LEGS_NO_SKIN.getIndex()] = (Entry)choice_basic_legs.getValue();
                    render();
                } else
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[((Entry)o).getLayer().getIndex()] = null;
            }
        });
        choice_main.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_sub.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_glove.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_boot.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_neck.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        choice_cape.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (t1 != null) {
                    Entry entry = (Entry)t1;
                    spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()[entry.getLayer().getIndex()] = entry;
                    render();
                }
            }
        });
        text_search.setLeft(new FontAwesome().create(FontAwesome.Glyph.SEARCH).color(SpriteTool.accentColor).size(15));
        text_search.setRight(new FontAwesome().create(FontAwesome.Glyph.CLOSE).color(SpriteTool.accentColor).size(15));
        text_search.getRight().setVisible(false);
        text_search.getRight().setOnMouseClicked(e -> {
            if (text_search.getRight().isVisible()) {
                text_search.setText("");
            }
        });
        text_search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()) {
                    text_search.getRight().setVisible(false);
                    text_search.getRight().setCursor(Cursor.DEFAULT);
                } else {
                    text_search.getRight().setVisible(true);
                    text_search.getRight().setCursor(Cursor.HAND);

                }

            }
        });

        //--------- Menu buttons
       // root.getStylesheets().clear();
        button_new_workspace.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS).color(SpriteTool.accentColor).size(20));
        button_new_workspace.setOnMouseClicked(e -> {
            spriteTool.createWorkspace();
        });
        button_open_workspace.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FOLDER_OPEN_ALT).color(SpriteTool.accentColor).size(20));
        button_open_workspace.setOnMouseClicked(e -> {
            spriteTool.openWorkspace();
        });
        button_save_workspace.setGraphic(new FontAwesome().create(FontAwesome.Glyph.SAVE).color(SpriteTool.accentColor).size(20));
        button_save_workspace.setOnMouseClicked(e -> {
            Subspace ss = (Subspace)list_subspaces.getSelectionModel().getSelectedItem();
            Entry entry = (Entry)list_entries.getItems().get(spriteTool.getWorkingCopyIndex());

            if (ss == null || entry == null)
                return;

            WorkspaceWriter wsWriter = new WorkspaceWriter(spriteTool.getWorkspace().getHome());

            if (wsWriter.updateEntry(ss, entry, spriteTool.getWorkingCopy())) {
                int index = ss.getEntryList().indexOf(entry);
                if (index > -1) {
                    ss.getEntryList().set(index, spriteTool.getWorkingCopy());
                }
            } else
                showError("There was a problem saving your changes.");

            checkSave();
        });
        button_pack.setGraphic(new FontAwesome().create(FontAwesome.Glyph.ARCHIVE).color(SpriteTool.accentColor).size(20));
        button_pack.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ORSC Archive", ".orsc");
            fileChooser.getExtensionFilters().add(filter);

            File archiveFile = fileChooser.showSaveDialog(root.getScene().getWindow());

            if (archiveFile == null)
                return;

            if (archiveFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, archiveFile.toString() + " already exists. Overwrite?");
                alert.showAndWait();
                if (alert.getResult() != ButtonType.OK)
                    return;

                archiveFile.delete();
            }

            //Packer packer = new Packer();
            //packer.pack(archiveFile, spriteTool.getWorkspace());
        });
        button_unpack.setGraphic(new FontAwesome().create(FontAwesome.Glyph.DROPBOX).color(SpriteTool.accentColor).size(20));
        button_unpack.setOnMouseClicked(e -> {

        });
        //--------- Other Buttons
        button_changepng.setGraphic(new FontAwesome().create(FontAwesome.Glyph.EDIT).color(SpriteTool.accentColor));
        button_changepng.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_female.setOnMouseClicked(e -> {
            choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fhead1"));
            choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fbody1"));
            choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
        });
        button_male.setOnMouseClicked(e -> {
            Entry hi = spriteTool.getWorkingCopy();
            Packer test = new Packer(hi);
            test.pack(new File("C:/temp/test/" + hi.getID()));
            Unpacker test2 = new Unpacker();
            Entry hey = test2.unpack(new File("C:/temp/test/" + hi.getID()));
            if (hi.equals(hey))
                System.out.println("butternut SQUASH!!!");
            //choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("head1"));
            //choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("body1"));
            //choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
        });
        button_addframe.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS).color(SpriteTool.accentColor));
        button_addframe.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLAY).color(Color.GREEN));
        button_play.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_play.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.STOP).color(Color.RED));
                    playAnimation();
                } else {
                    button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLAY).color(Color.GREEN));
                    stopAnimation();
                }
            }
        });

        slider_period.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (button_play.isSelected()) {
                    stopAnimation();
                    playAnimation();
                }
            }
        });
        button_male.setGraphic(new FontAwesome().create(FontAwesome.Glyph.MALE).color(spriteTool.accentColor));
        button_female.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FEMALE).color(spriteTool.accentColor));

        //--------- subspace list
        list_subspaces.setOnContextMenuRequested(event -> {
            JFXPopup popup = buildSubspaceMenu();
            if (popup != null)
                popup.show(spriteTool.getPrimaryStage(),event.getSceneX(), event.getSceneY(),JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT,0,0);
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
                if (!triggerListeners)
                    return;

                if (needSave()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discard unsaved changes?");
                    alert.showAndWait();
                    if (alert.getResult() != ButtonType.OK) {
                        triggerListeners = false;
                        list_entries.getSelectionModel().select(oldEntry);
                        triggerListeners = true;
                        return;
                    }
                }
                spriteTool.getSpriteRenderer().reset();
                if (newEntry == null) {
                    spriteTool.clearWorkingCopy();
                } else
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

                for (Frame frame : spriteTool.getWorkingCopy().getFrames()) {
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
        FilteredList<Entry> filteredEntryList = new FilteredList<>(ss.getEntryList(), s -> true);
        text_search.textProperty().addListener(obs-> {
            String filter = text_search.getText();
            if (filter == null || filter.isEmpty()) {
                filteredEntryList.setPredicate(s -> true);
            } else
                filteredEntryList.setPredicate(s -> s.toString().contains(filter));

            if (filteredEntryList.size() == 0)
                text_search.getStyleClass().add("textField-red");
            else
                text_search.getStyleClass().removeAll("textField-red");
        });
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
        btn_newCategory.setOnMouseClicked(e -> {
            popup.hide();
            try { spriteTool.spinCreateEntry(); } catch (IOException a) { a.printStackTrace(); return; }

            Stage stage = new Stage();
            Scene scene = new Scene(spriteTool.getCreateEntryRoot());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        });
        buttons.add(btn_newCategory);

        for (JFXButton button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPadding(new Insets(8));
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttons);

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

                File path = new File(spriteTool.getWorkspace().getHome().toString(), ss.getName());
                File newPath = new File(path.getParent(), td.getEditor().getText());

                if (newPath.exists()) {
                    showError("That category already exists.");
                    return;
                }

                if (!path.renameTo(newPath)) {
                    showError("Error renaming category.");
                    return;
                }

                ss.setName(td.getEditor().getText());
            });
            buttons.add(btn_delCategory);
            buttons.add(btn_renameCategory);
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttons);

        for (JFXButton button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPadding(new Insets(8));
        }

        popup.setPopupContent(vbox);
        return popup;
    }

    private void showEntry(Entry entry) {
        triggerListeners = false;
        scroll_canvas.setValue(1);
        scroll_canvas.setDisable(false);
        scroll_zoom.setValue(0);
        if (entry == null) {
            check_render.setDisable(false);
            scroll_canvas.setMax(1);
        } else {
            scroll_canvas.setMax(entry.frameCount());
            if (entry.getLayer() == null) {
                check_render.setSelected(false);
                check_render.setDisable(true);
            }
        }

        showEntry(entry, 0);
    }

    private void showEntry(Entry newEntry, int frame) {
        spriteTool.getSpriteRenderer().clear();
        spriteTool.getSpriteRenderer().wipeBuffer();
        triggerListeners = false;
        if (newEntry != null) {
            Frame sprite = newEntry.getFrame(frame);

            if (sprite == null)
                return;

            Info info = sprite.getInfo();
            text_name.setText(newEntry.getID());
            check_shift.setSelected(info.getUseShift());
            text_hshift.setText(String.valueOf(info.getOffsetX()));
            text_vshift.setText(String.valueOf(info.getOffsetX()));
            text_boundh.setText(String.valueOf(info.getBoundHeight()));
            text_boundw.setText(String.valueOf(info.getBoundWidth()));
            label_frame.setText(info.getFrame() + " / " + info.getFrameCount());
            choice_type.setValue(info.getType());
            choice_layer.setValue(info.getLayer());

        } else {
            text_name.clear();
            check_shift.setSelected(false);
            text_hshift.clear();
            text_vshift.clear();
            text_boundh.clear();
            text_boundw.clear();
            label_frame.setText("");
            choice_type.setValue(null);
            choice_layer.getItems().clear();
        }

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
        playTimer.schedule(playTask, 200, (int)slider_period.getValue());
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

    public boolean needSave() { return needSave((Subspace)list_subspaces.getSelectionModel().getSelectedItem()); }
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

    private Frame getWorkingSprite() {
        int index = (int)scroll_canvas.getValue()-1;
        if (spriteTool.getWorkingCopy() == null)
            return null;

        return spriteTool.getWorkingCopy().getFrame(index);
    }

    private void render() {
        int frame = (int)scroll_canvas.getValue()-1;
        spriteTool.getSpriteRenderer().wipeBuffer();
        spriteTool.getSpriteRenderer().clear();
        if (check_render.selectedProperty().getValue()) {
            Entry override = null;

            if (spriteTool.getWorkingCopy() != null) {
                if (spriteTool.getWorkingCopy().getLayer() != null)
                    override = spriteTool.getWorkingCopy();
            }

            spriteTool.getSpriteRenderer().renderPlayer(frame, override, color_grayscale.getValue(), color_bluescale.getValue());
        }
        else
            spriteTool.getSpriteRenderer().renderSprite(getWorkingSprite(), color_grayscale.getValue(), color_bluescale.getValue());
    }

    public Subspace getCurrentSubspace() { return (Subspace)list_subspaces.getSelectionModel().getSelectedItem(); }
    public void loadChoiceBoxes() {
        Platform.runLater(() -> {
            choice_basic_head.getItems().clear();
            choice_basic_head.getItems().add(null);
            choice_basic_body.getItems().clear();
            choice_basic_legs.getItems().clear();
            choice_head.getItems().clear();
            choice_head.getItems().add(null);
            choice_body.getItems().clear();
            choice_body.getItems().add(null);
            choice_legs.getItems().clear();
            choice_legs.getItems().add(null);
            choice_main.getItems().clear();
            choice_main.getItems().add(null);
            choice_sub.getItems().clear();
            choice_sub.getItems().add(null);
            choice_glove.getItems().clear();
            choice_glove.getItems().add(null);
            choice_boot.getItems().clear();
            choice_neck.getItems().clear();
            choice_cape.getItems().clear();
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
                                case PLAYER_EQUIPPABLE_NOCOMBAT:
                                    switch (entry.getLayer()) {
                                        case HEAD_NO_SKIN:
                                        case HEAD_WITH_SKIN:
                                            choice_head.getItems().add(entry);
                                            break;
                                        case BODY_NO_SKIN:
                                        case BODY_WITH_SKIN:
                                            choice_body.getItems().add(entry);
                                            break;
                                        case LEGS_NO_SKIN:
                                        case LEGS_WITH_SKIN:
                                            choice_legs.getItems().add(entry);
                                            break;
                                        case MAIN_HAND:
                                            choice_main.getItems().add(entry);
                                            break;
                                        case OFF_HAND:
                                            choice_sub.getItems().add(entry);
                                            break;
                                        case GLOVES:
                                            choice_glove.getItems().add(entry);
                                            break;
                                        case BOOTS:
                                            choice_boot.getItems().add(entry);
                                            break;
                                        case NECK:
                                            choice_neck.getItems().add(entry);
                                            break;
                                        case CAPE:
                                            choice_cape.getItems().add(entry);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        });

    }
}
