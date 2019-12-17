package com.OpenRSC.Interface.SpriteTool;

import com.OpenRSC.IO.Archive.Packer;
import com.OpenRSC.IO.Archive.Unpacker;
import com.OpenRSC.IO.Image.ColorDecimator;
import com.OpenRSC.IO.Image.ImageReader;
import com.OpenRSC.IO.Image.ImageWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.PlayerRenderer;
import com.OpenRSC.SpriteTool;
import com.jfoenix.controls.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
//TODO: search for entry, select entry, save, entry gets deselected
//TODO: add +/- buttons for adjusting offsets
//TODO: add ability to clone entry
//TODO: add lucki's new stuff
public class Controller implements Initializable {
    private SpriteTool spriteTool;
    private boolean triggerListeners = true;

    @FXML
    private VBox root;

    @FXML
    private JFXListView list_subspaces, list_entries;

    @FXML
    private Label label_status, label_frame, label_color_count;

    @FXML
    private JFXCheckBox check_shift, check_render;

    @FXML
    private TextField text_name, text_vshift, text_hshift, text_boundw, text_boundh;

    @FXML
    private CustomTextField text_search;

    @FXML
    private ImageView canvas, image_logo;

    @FXML
    private ScrollBar scroll_canvas, scroll_zoom;

    @FXML
    private JFXButton button_new_workspace, button_open_workspace, button_save_workspace, button_changeallframes, button_changepng, button_male, button_female, button_export,
            button_copy_colors, button_decimate, button_fit, button_pack_archive, button_vshift_inc, button_vshift_dec, button_hshift_inc, button_hshift_dec;

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
                    choice_layer.getItems().addAll(((Entry.TYPE) t1).getLayers());
            }
        });

        choice_basic_head.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_basic_body.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_basic_legs.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_head.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_body.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_legs.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_main.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_sub.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_glove.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_boot.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_neck.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
            }
        });
        choice_cape.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updatePlayerLook();
                render();
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
        button_save_workspace.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_save_workspace.setOnMouseClicked(e -> {
            if (spriteTool.getWorkspace() == null ||
                    getCurrentSubspace() == null)
                return;

            if (!button_decimate.isDisable()) {
                showError("Before saving, please reduce the unique color count.");
                return;
            }
            Subspace ss = getCurrentSubspace();
            Entry entry = spriteTool.getWorkingCopy();

            File osprNew = new File(ss.getHome().toString(), entry.getID() + ".ospr");
            File osprOld = new File(ss.getHome().toString(), ss.getEntryList().get(spriteTool.getWorkingCopyIndex()).getID() + ".ospr");

            if (!entry.getID().equals(spriteTool.getWorkingCopy().getID()) && osprNew.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "That name already exists in this subspace.");
                alert.showAndWait();
                return;
            }

            if (osprOld.exists())
                osprOld.delete();

            Packer packer = new Packer();

            if (packer.packEntry(entry, osprNew)) {
                ss.getEntryList().set(spriteTool.getWorkingCopyIndex(), spriteTool.getWorkingCopy());
            } else
                showError("There was a problem saving your changes.");

            checkSave();
        });

        button_pack_archive.setGraphic(new FontAwesome().create(FontAwesome.Glyph.ARCHIVE).color(SpriteTool.accentColor).size(20));
        button_pack_archive.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(spriteTool.getWorkspace().getHome().getParent().toFile());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OSAR file", ".osar"));
            File archive = fileChooser.showSaveDialog(root.getScene().getWindow());

            if (archive == null)
                return;

            Packer packer = new Packer();
            packer.packArchive(spriteTool.getWorkspace(), archive);
        });
        //--------- Other Buttons
        button_vshift_inc.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS).color(SpriteTool.accentColor).size(8));
        button_vshift_inc.disableProperty().bind(text_vshift.textProperty().isEmpty());
        button_vshift_inc.setOnMouseClicked(e -> {
            int value = Integer.parseInt(text_vshift.getText());
            text_vshift.setText(String.valueOf(++value));
        });
        button_vshift_dec.setGraphic(new FontAwesome().create(FontAwesome.Glyph.MINUS).color(SpriteTool.accentColor).size(8));
        button_vshift_dec.disableProperty().bind(text_vshift.textProperty().isEmpty());
        button_vshift_dec.setOnMouseClicked(e -> {
            int value = Integer.parseInt(text_vshift.getText());
            text_vshift.setText(String.valueOf(--value));
        });
        button_hshift_inc.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS).color(SpriteTool.accentColor).size(8));
        button_hshift_inc.disableProperty().bind(text_hshift.textProperty().isEmpty());
        button_hshift_inc.setOnMouseClicked(e -> {
            int value = Integer.parseInt(text_hshift.getText());
            text_hshift.setText(String.valueOf(++value));
        });
        button_hshift_dec.setGraphic(new FontAwesome().create(FontAwesome.Glyph.MINUS).color(SpriteTool.accentColor).size(8));
        button_hshift_dec.disableProperty().bind(text_hshift.textProperty().isEmpty());
        button_hshift_dec.setOnMouseClicked(e -> {
            int value = Integer.parseInt(text_hshift.getText());
            text_hshift.setText(String.valueOf(--value));
        });
        button_fit.setOnMouseClicked(e -> {
            if (spriteTool.getWorkingCopy() != null) {
                getWorkingFrame().changeBoundWidth(getWorkingFrame().getWidth());
                getWorkingFrame().changeBoundHeight(getWorkingFrame().getHeight());
                showEntry(spriteTool.getWorkingCopy(), (int)scroll_canvas.getValue()-1);
                render();
                checkSave();
            }
        });
        button_decimate.setDisable(true);
        button_decimate.setOnMouseClicked(e -> {
            if (spriteTool.getWorkingCopy().getType() != Entry.TYPE.SPRITE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please make sure ALL frames have their FINAL images " + "set before attempting to fix colors.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                if (alert.getResult() != ButtonType.OK)
                    return;
            }
                SimpleDoubleProperty ratio = new SimpleDoubleProperty();
                progress_bar.progressProperty().bind(ratio);
                spriteTool.showPleaseWait();
                final Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        ColorDecimator colorDecimator = new ColorDecimator();
                        colorDecimator.reduceColorPalette(spriteTool.getWorkingCopy().getUniqueColors(), 256, ratio);

                        ratio.set(0);
                        for (int i=0; i<spriteTool.getWorkingCopy().getFrames().length; ++i) {
                            Frame frame = spriteTool.getWorkingCopy().getFrames()[i];
                            frame.changePixels(colorDecimator.decimatePixelColors(frame.getPixels()));
                            ratio.set((double)i/spriteTool.getWorkingCopy().getFrames().length);
                        }

                        Platform.runLater(() -> {
                            label_color_count.setText(spriteTool.getWorkingCopy().getUniqueColors().size() + " / 256");
                            checkSave();
                            render();
                            ratio.set(1);
                            progress_bar.progressProperty().unbind();
                            spriteTool.clearPleaseWait();
                        });
                        return null;
                    }
                };

                Thread thread = new Thread(task, "task-thread");
                thread.start();

        });
        button_export.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PHOTO).color(SpriteTool.accentColor).size(20));
        button_export.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_export.setOnMouseClicked(e -> {
            if (spriteTool.getWorkingCopy() == null)
                return;

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());

            File saveDir = directoryChooser.showDialog(root.getScene().getWindow());
            if (saveDir == null ||
                !saveDir.exists())
                return;
            File home = new File(saveDir.toString(), spriteTool.getWorkingCopy().getID());
            SimpleDoubleProperty ratio = new SimpleDoubleProperty();
            progress_bar.progressProperty().bind(ratio);
            spriteTool.showPleaseWait();
            final Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i=0; i<spriteTool.getWorkingCopy().getFrames().length; ++i) {
                        ImageWriter imageWriter = new ImageWriter();
                        imageWriter.write(new File(home.toString(), (i+1) + ".png"), spriteTool.getWorkingCopy().getFrames()[i]);
                        ratio.set((double)(i+1)/spriteTool.getWorkingCopy().getFrames().length);
                    }
                    Platform.runLater(() -> {
                        ratio.set(1);
                        progress_bar.progressProperty().unbind();
                        spriteTool.clearPleaseWait();
                    });
                    return null;
                }
            };

            Thread thread = new Thread(task, "task-thread");
            thread.start();
        });
        button_copy_colors.setGraphic(new FontAwesome().create(FontAwesome.Glyph.COPY).color(SpriteTool.accentColor));
        button_copy_colors.setOnMouseClicked(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection text = new StringSelection("Grayscale: 0x" + color_grayscale.getValue().toString().substring(4).toUpperCase() + "\nBluescale: 0x" + color_bluescale.getValue().toString().substring(4).toUpperCase());
            clipboard.setContents(text,null);
        });
        button_changepng.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PENCIL).color(SpriteTool.accentColor));
        button_changepng.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_changepng.setOnMouseClicked(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(getCurrentSubspace().getHome().toFile());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images (.png)", "*.png"));
            File imageFile = fileChooser.showOpenDialog(root.getScene().getWindow());

            if (imageFile == null || !imageFile.exists())
                return;

            ImageReader imageReader = new ImageReader();
            imageReader.read(imageFile);

            Frame frame = spriteTool.getWorkingCopy().getFrames()[(int)scroll_canvas.getValue()-1];
            frame.changeDimensions(imageReader.getWidth(), imageReader.getHeight());
            frame.changePixels(imageReader.getPixels());
            label_color_count.setText(spriteTool.getWorkingCopy().getUniqueColors().size() + " / 256");
            render();
            checkSave();
        });
        button_female.setOnMouseClicked(e -> {
            Unpacker unpacker = new Unpacker();
            unpacker.unpackArchive(new File("C:/temp/baller.osar"));
//            choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fhead1"));
//            choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("fbody1"));
//            choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
        });
        button_male.setOnMouseClicked(e -> {
//            File subspaceHome = new File(spriteTool.getWorkspace().getHome().getParent().toString(), "goku");
//            for (Subspace subspace : spriteTool.getWorkspace().getSubspaces()) {
//                File writeTo = new File(subspaceHome.toString(), subspace.getName());
//                writeTo.mkdirs();
//                for (Entry entry : subspace.getEntryList()) {
//                    Packer packer = new Packer(entry);
//                    File file = new File(writeTo.toString(), entry.getID() + ".ospr");
//                    packer.pack(file);
//                }
//
//            }
            choice_basic_head.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("head1"));
            choice_basic_body.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("body1"));
            choice_basic_legs.getSelectionModel().select(spriteTool.getSpriteRenderer().getPlayerRenderer().getShippedAnimations().getEntryByName("legs1"));
        });
        button_changeallframes.setGraphic(new FontAwesome().create(FontAwesome.Glyph.FOLDER_ALT).color(SpriteTool.accentColor));
        button_changeallframes.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_changeallframes.setOnMouseClicked(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());

            File frameFolder = directoryChooser.showDialog(root.getScene().getWindow());

            if (frameFolder == null ||
                !frameFolder.exists())
                return;

            File[] frameFiles = frameFolder.listFiles(File::isFile);

            Pattern p = Pattern.compile("\\d+");
            Matcher m;
            for (File frameFile : frameFiles) {
                if (!FilenameUtils.getExtension(frameFile.getName()).equalsIgnoreCase("png"))
                    continue;

                String name = FilenameUtils.removeExtension(frameFile.getName());
                m = p.matcher(name);
                int frameIndex = -1;
                if (m.find()) {
                    frameIndex = Integer.parseInt(m.group());
                    if (frameIndex > spriteTool.getWorkingCopy().getFrames().length ||
                        frameIndex < 1)
                        continue;
                    ImageReader imageReader = new ImageReader();
                    imageReader.read(frameFile);

                    Frame frame = spriteTool.getWorkingCopy().getFrames()[frameIndex-1];
                    frame.changeDimensions(imageReader.getWidth(), imageReader.getHeight());
                    frame.changePixels(imageReader.getPixels());
                }

                label_color_count.setText(spriteTool.getWorkingCopy().getUniqueColors().size() + " / 256");
                render();
                checkSave();
            }

        });
        button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLAY).color(Color.GREEN));
        button_play.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        button_play.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    playAnimation();
                } else {
                    stopAnimation();
                }
            }
        });
        label_color_count.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1 == null ||
                    t1.isEmpty())
                    return;

                String[] values = t1.split(Pattern.quote(" / "));
                if (Integer.valueOf(values[0]) > Integer.valueOf(values[1])) {
                    label_color_count.setTextFill(Color.RED);
                    button_decimate.setDisable(false);
                } else {
                    label_color_count.setTextFill(Color.WHITE);
                    button_decimate.setDisable(true);
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
                popup.show(spriteTool.getPrimaryStage(), event.getSceneX(), event.getSceneY(), JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT, 0, 0);
        });

        list_subspaces.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Subspace>() {
            @Override
            public void changed(ObservableValue<? extends Subspace> observableValue, Subspace oldSubspace, Subspace newSubspace) {
                if (newSubspace == null ||
                        !triggerListeners)
                    return;

                stopAnimation();

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
                popup.show(spriteTool.getPrimaryStage(), event.getSceneX(), event.getSceneY(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, 0);
        });

        list_entries.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observableValue, Entry oldEntry, Entry newEntry) {
                if (!triggerListeners)
                    return;

                stopAnimation();

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
                    spriteTool.setWorkingCopy(getCurrentSubspace().getEntryList().indexOf(newEntry), newEntry.clone());
                checkSave();
                showEntry(spriteTool.getWorkingCopy());
                render();
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

        image_logo.setImage(new Image("file:resource/logo.png"));
        canvas.setOnScroll(e -> {
            if (e.isControlDown()) {
                if (scroll_canvas.getMax() == 1)
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
                double newValue = scroll_zoom.getValue() - 4 * Math.signum(e.getDeltaY());
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
            double deltaX = (e.getSceneX() - point.getX()) * viewPort.getWidth() / canvas.getFitWidth();
            double deltaY = (e.getSceneY() - point.getY()) * viewPort.getHeight() / canvas.getFitHeight();
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

                Rectangle2D viewportRect = new Rectangle2D(-t1.intValue(), -t1.intValue(), 300 + 2 * t1.intValue(), 300 + 2 * t1.intValue());
                canvas.setViewport(viewportRect);
            }
        });
        scroll_canvas.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scroll_canvas.setValue(t1.intValue());
                if (scroll_canvas.getValue() == number.intValue())
                    return;

                showEntry(spriteTool.getWorkingCopy(), t1.intValue() - 1);
                render();
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

                getWorkingFrame().changeUseShift(t1);
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

                spriteTool.getWorkingCopy().changeID(t1);
                checkSave();
            }
        });
        text_boundh.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
        text_boundh.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!triggerListeners)
                    return;

                if (t1 == null || t1.isEmpty() || t1.equalsIgnoreCase("-"))
                    return;

                if (!t1.matches("^[-+]?\\d+$")) {
                    text_boundh.setText(s);
                    return;
                }

                int value = Integer.parseInt(t1);
                if (value > canvas.getFitWidth() - 2 ||
                        value <= 0)
                    return;

                getWorkingFrame().changeBoundHeight(value);

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

                if (t1 == null || t1.isEmpty() || t1.equalsIgnoreCase("-"))
                    return;

                if (!t1.matches("^[-+]?\\d+$")) {
                    text_boundw.setText(s);
                    return;
                }

                int value = Integer.parseInt(t1);
                if (value > canvas.getFitWidth() - 2 ||
                        value <= 0)
                    return;

                getWorkingFrame().changeBoundWidth(value);
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

                if (t1 == null || t1.isEmpty() || t1.equalsIgnoreCase("-"))
                    return;

                if (!t1.matches("^[-+]?\\d+$")) {
                    text_hshift.setText(s);
                    return;
                }

                getWorkingFrame().changeOffsetX(Integer.parseInt(t1));
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

                if (t1 == null || t1.isEmpty() || t1.equalsIgnoreCase("-"))
                    return;

                if (!t1.matches("^[-+]?\\d+$")) {
                    text_vshift.setText(s);
                    return;
                }

                getWorkingFrame().changeOffsetY(Integer.parseInt(t1));
                getWorkingFrame().changeOffsetY(Integer.parseInt(t1));
                checkSave();

                render();
            }
        });
        text_name.disableProperty().bind(list_entries.getSelectionModel().selectedItemProperty().isNull());
    }

    //------------------ private methods
    private void populateEntryList(Subspace ss) {
        FilteredList<Entry> filteredEntryList = new FilteredList<>(ss.getEntryList(), s -> true);
        text_search.textProperty().addListener(obs -> {
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
        list_entries.setItems(null);
        this.triggerListeners = false;
        this.list_subspaces.getItems().clear();
        this.list_subspaces.setItems(ws.getSubspaces());
        this.triggerListeners = true;
    }

    private JFXPopup buildEntryMenu() {
        if (spriteTool.getWorkspace() == null
                || list_subspaces.getSelectionModel().getSelectedItem() == null)
            return null;

        JFXPopup popup = new JFXPopup();
        List<JFXButton> buttons = new ArrayList<>();
        JFXButton btn_newEntry = new JFXButton("New Entry");
        btn_newEntry.setOnMouseClicked(e -> {
            popup.hide();
            try {
                spriteTool.spinCreateEntry();
            } catch (IOException a) {
                a.printStackTrace();
                return;
            }

            Stage stage = new Stage();
            Scene scene = new Scene(spriteTool.getCreateEntryRoot());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        });
        buttons.add(btn_newEntry);

        if (list_entries.getSelectionModel().getSelectedItem() != null) {
            Entry selectedEntry = (Entry)list_entries.getSelectionModel().getSelectedItem();
            JFXButton btn_cloneEntry = new JFXButton("Clone " + selectedEntry.getID());
            btn_cloneEntry.setOnMouseClicked(e -> {
                popup.hide();
                Entry toClone = (Entry)list_entries.getSelectionModel().getSelectedItem();
                Subspace subspace = (Subspace)list_subspaces.getSelectionModel().getSelectedItem();
                if (toClone == null ||
                    subspace == null)
                    return;

                Entry clone = toClone.clone();
                clone.changeID(clone.getID() + "_clone");
                subspace.getEntryList().add(clone);
                File location = new File(subspace.getHome().toString(), clone.getID() + ".ospr");
                Packer packer = new Packer();
                packer.packEntry(clone, location);
            });
            buttons.add(btn_cloneEntry);
        }

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
        Subspace ss = getCurrentSubspace();
        if (ss != null) {
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

        } else {


        }

        showEntry(entry, 0);
    }

    private void showEntry(Entry newEntry, int frameIndex) {
        spriteTool.getSpriteRenderer().clear();
        spriteTool.getSpriteRenderer().wipeBuffer();
        triggerListeners = false;
        if (newEntry != null) {
            Frame frame = newEntry.getFrames()[frameIndex];

            if (frame == null)
                return;

            text_name.setText(newEntry.getID());
            check_shift.setSelected(frame.getUseShift());
            text_hshift.setText(String.valueOf(frame.getOffsetX()));
            text_vshift.setText(String.valueOf(frame.getOffsetY()));
            text_boundh.setText(String.valueOf(frame.getBoundHeight()));
            text_boundw.setText(String.valueOf(frame.getBoundWidth()));
            label_frame.setText(frameIndex + 1 + " / " + newEntry.getFrames().length);
            label_color_count.setText(newEntry.getUniqueColors().size() + " / 256");
            choice_type.setValue(newEntry.getType());
            choice_layer.setValue(newEntry.getLayer());
            scroll_canvas.setMax(newEntry.getFrames().length);
            if (newEntry.getLayer() == null) {
                check_render.setSelected(false);
                check_render.setDisable(true);
            } else {
                check_render.setDisable(false);
            }

        } else {
            text_name.clear();
            check_shift.setSelected(false);
            text_hshift.clear();
            text_vshift.clear();
            text_boundh.clear();
            text_boundw.clear();
            label_frame.setText("");
            label_color_count.setText("");
            choice_type.setValue(null);
            choice_layer.getItems().clear();
            scroll_canvas.setMax(1);
            check_render.setDisable(false);
        }

        triggerListeners = true;
    }

    private void playAnimation() {
        playTask = new TimerTask() {
            public void run() {
                int curFrame = (int) scroll_canvas.getValue();
                if (curFrame == (int) scroll_canvas.getMax())
                    Platform.runLater(() -> scroll_canvas.setValue(1));
                else
                    Platform.runLater(() -> scroll_canvas.setValue(curFrame + 1));
            }
        };
        playTimer.schedule(playTask, 100, (int) slider_period.getValue());

        button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.STOP).color(Color.RED));
    }

    private void stopAnimation() {
        if (playTask != null) {
            playTask.cancel();
            button_play.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLAY).color(Color.GREEN));
        }
    }

    private void checkSave() {
        if (needSave())
            button_save_workspace.getStyleClass().addAll("button-red", "edit-icon");
        else
            button_save_workspace.getStyleClass().removeAll("button-red", "edit-icon");
    }

    private void updatePlayerLook() {
        Entry basichead = (Entry)choice_basic_head.getValue();
        Entry basicbody = (Entry)choice_basic_body.getValue();
        Entry basiclegs = (Entry)choice_basic_legs.getValue();
        Entry helm = (Entry)choice_head.getValue();
        Entry body = (Entry)choice_body.getValue();
        Entry legs = (Entry)choice_legs.getValue();

        spriteTool.getSpriteRenderer().getPlayerRenderer().updateLook(
                (helm != null && helm.getLayer() == PlayerRenderer.LAYER.HEAD_NO_SKIN) ? helm : basichead,
                (body != null && body.getLayer() == PlayerRenderer.LAYER.BODY_NO_SKIN) ? body : basicbody,
                (legs != null && legs.getLayer() == PlayerRenderer.LAYER.LEGS_NO_SKIN) ? legs : basiclegs,
                (Entry)choice_main.getValue(),
                (Entry)choice_sub.getValue(),
                (helm != null && helm.getLayer() == PlayerRenderer.LAYER.HEAD_WITH_SKIN) ? helm : null,
                (body != null && body.getLayer() == PlayerRenderer.LAYER.BODY_WITH_SKIN) ? body : null,
                (legs != null && legs.getLayer() == PlayerRenderer.LAYER.LEGS_WITH_SKIN) ? legs : null,
                (Entry)choice_neck.getValue(),
                (Entry)choice_boot.getValue(),
                (Entry)choice_glove.getValue(),
                (Entry)choice_cape.getValue()
        );

        if (check_render.selectedProperty().getValue()) {
            int maxFrames = 1;
            for (Entry entry : spriteTool.getSpriteRenderer().getPlayerRenderer().getLayers()) {
                if (entry != null &&
                        entry.getFrames().length > maxFrames)
                    maxFrames = entry.getFrames().length;
            }
            scroll_canvas.setMax(maxFrames);
        }
    }
    //------------------- public methods
    public void setSpriteTool(SpriteTool spriteTool) {
        this.spriteTool = spriteTool;
    }

    public void setStatus(String status) {
        label_status.setText(status);
    }

    public ImageView getCanvas() {
        return this.canvas;
    }

    public void showError(String text) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText(text);
        error.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }

    public boolean needSave() {
        return needSave(getCurrentSubspace());
    }

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

    private Frame getWorkingFrame() {
        int index = (int) scroll_canvas.getValue() - 1;
        if (spriteTool.getWorkingCopy() == null)
            return null;

        return spriteTool.getWorkingCopy().getFrames()[index];
    }

    private void render() {
        int frame = (int) scroll_canvas.getValue() - 1;
        spriteTool.getSpriteRenderer().wipeBuffer();
        spriteTool.getSpriteRenderer().clear();
        if (check_render.selectedProperty().getValue()) {
            Entry override = null;

            if (spriteTool.getWorkingCopy() != null) {
                if (spriteTool.getWorkingCopy().getLayer() != null)
                    override = spriteTool.getWorkingCopy();
            }

            spriteTool.getSpriteRenderer().renderPlayer(frame, override, color_grayscale.getValue(), color_bluescale.getValue());
        } else
            spriteTool.getSpriteRenderer().renderSprite(getWorkingFrame(), color_grayscale.getValue(), color_bluescale.getValue());
    }

    public Subspace getCurrentSubspace() {
        return (Subspace) list_subspaces.getSelectionModel().getSelectedItem();
    }

    public void loadChoiceBoxes() {
        Platform.runLater(() -> {
            choice_basic_head.getItems().clear();
            choice_basic_head.getItems().add(null);
            choice_basic_body.getItems().clear();
            choice_basic_body.getItems().add(null);
            choice_basic_legs.getItems().clear();
            choice_basic_legs.getItems().add(null);
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
            choice_boot.getItems().add(null);
            choice_neck.getItems().clear();
            choice_neck.getItems().add(null);
            choice_cape.getItems().clear();
            choice_cape.getItems().add(null);
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
