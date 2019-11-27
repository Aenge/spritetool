package com.OpenRSC;
import com.OpenRSC.IO.Workspace.WorkspaceReader;
import com.OpenRSC.Interface.SpriteTool.Controller;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.SpriteRenderer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SpriteTool extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //SpriteTool spriteTool = new SpriteTool(primaryStage);
        //spriteTool.start();
        this.primaryStage = primaryStage;
        initLoaders();
        start();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;
    private Workspace workspace;

    private Parent splashRoot;
    private com.OpenRSC.Interface.Splash.Controller splashController;
    private VBox menuRoot;
    private com.OpenRSC.Interface.PopMenu.Controller menuController;
    private Parent mainRoot;
    private com.OpenRSC.Interface.SpriteTool.Controller mainController;
    private Parent createWorkspaceRoot;
    private com.OpenRSC.Interface.CreateWorkspace.Controller createWorkspaceController;
    private SpriteRenderer spriteRenderer;

    private Entry workingCopy;
    private int workingCopyIndex = -1;

    public void start() {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(getSplashRoot(), 675, 400));
        primaryStage.show();
    }

    private void initLoaders() {
        try {
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Interface/Splash/Splash.fxml"));
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("Interface/PopMenu/PopMenu.fxml"));
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Interface/SpriteTool/SpriteTool.fxml"));
            FXMLLoader createWorkspaceLoader = new FXMLLoader(getClass().getResource("Interface/CreateWorkspace/CreateWorkspace.fxml"));
            this.splashRoot = splashLoader.load();
            this.menuRoot = menuLoader.load();
            this.mainRoot = mainLoader.load();
            this.createWorkspaceRoot = createWorkspaceLoader.load();
            this.menuRoot.setStyle("-fx-background-color: #3C3C3C");
            this.splashController = splashLoader.getController();
            this.menuController = menuLoader.getController();
            this.mainController = mainLoader.getController();
            this.createWorkspaceController = createWorkspaceLoader.getController();
            this.splashController.setSpriteTool(this);
            this.menuController.setSpriteTool(this);
            this.mainController.setSpriteTool(this);
            this.createWorkspaceController.setSpriteTool(this);
        } catch (IOException a) {
            a.printStackTrace();
        }

    }

    public void closeSplash() {
        Stage newStage = new Stage();
        newStage.setTitle("OpenRSC Sprite Tool");

        Scene scene = new Scene(this.mainRoot, 750, 540);
        newStage.setScene(scene);
        newStage.setOnCloseRequest(e -> {
            getMainController().stopTimer();
        });
        newStage.setResizable(false);
        newStage.show();
        primaryStage.hide();

        setPrimaryStage(newStage);
        spriteRenderer = new SpriteRenderer(mainController.getCanvas());
        spriteRenderer.setSpriteTool(this);

        getMainController().loadChoiceBoxes();

        getMainController().getRoot().requestFocus();
    }

    public void createWorkspace() {
        if (workspace != null)
            return;

        Stage stage = new Stage();
        Scene scene = new Scene(createWorkspaceRoot);

        stage.setScene(scene);
        stage.show();
    }

    public void openWorkspace() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        //Check if they closed the dialog without choosing
        if (selectedDirectory == null) {
            return;
        }

        //Check if the path exists
        if (!selectedDirectory.exists()) {
            System.out.print("Invalid directory.");
            return;
        }

        final int maxProgress = getFilesCount(selectedDirectory);
        SimpleIntegerProperty countProgress = new SimpleIntegerProperty();
        SimpleDoubleProperty ratio = new SimpleDoubleProperty();
        countProgress.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ratio.set(t1.doubleValue()/maxProgress);
            }
        });
        mainController.progress_bar.progressProperty().bind(ratio);
        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                openWorkspace(selectedDirectory.toPath(), countProgress);
                return null;
            }
        };


        Thread thread = new Thread(task, "task-thread");
        thread.start();
    }

    public void openWorkspace(Path path) { openWorkspace(path, null); }
    public void openWorkspace(Path path, SimpleIntegerProperty countProgress) {
        WorkspaceReader reader = new WorkspaceReader();
        reader.setProgressCounter(countProgress);
        this.workspace = reader.loadWorkspace(path);

        if (this.workspace == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open workspace.");
            alert.showAndWait();
            return;
        }

        //Display the workspace
        Platform.runLater(() -> {
            mainController.populateSubspaceList(this.workspace);
            mainController.setStatus("Workspace: " + workspace.getName() + ": " + workspace.getSubspaceCount() + " categories, " + workspace.getEntryCount() +
                    " entries (" + workspace.getSpriteCount() + " sprites, " + workspace.getAnimationCount() + " animations)");
        });

        return;
    }

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }
    public Stage getPrimaryStage() { return this.primaryStage; }

    public Workspace getWorkspace() { return this.workspace; }
    public Parent getSplashRoot() { return this.splashRoot; }
    public Parent getMainRoot() { return this.mainRoot; }
    public VBox getMenuRoot() { return this.menuRoot; }
    public Parent getCreateWorkspaceRoot() { return this.createWorkspaceRoot; }
    public com.OpenRSC.Interface.Splash.Controller getSplashController() { return this.splashController; }
    public com.OpenRSC.Interface.PopMenu.Controller getMenuController() { return this.menuController; }
    public Controller getMainController() { return this.mainController; }
    public SpriteRenderer getSpriteRenderer() { return this.spriteRenderer; }

    public Entry getWorkingCopy() { return this.workingCopy; }
    public int getWorkingCopyIndex() { return this.workingCopyIndex; }


    public void clearWorkingCopy() {
        this.workingCopy = null;
        this.workingCopyIndex = -1;
    }
    public void setWorkingCopy(Subspace ss, Entry entry) {
        this.workingCopy = entry;
        this.workingCopyIndex = ss.getEntryList().indexOf(entry);
    }

    public static int getFilesCount(File file) {
        File[] files = file.listFiles();
        int count = 0;
        for (File f : files)
            if (f.isDirectory())
                count += getFilesCount(f);
            else
                count++;

        return count;
    }
}
