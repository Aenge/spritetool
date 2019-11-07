package com.SpriteTool;
import com.SpriteTool.IO.WorkspaceReader;
import com.SpriteTool.Model.Workspace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class SpriteTool extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //SpriteTool spriteTool = new SpriteTool(primaryStage);
        //spriteTool.start();
        this.primaryStage = primaryStage;
        initLoaders();
        start();
    }

    private Stage primaryStage;
    private Workspace workspace;

    private Parent splashRoot;
    private com.SpriteTool.Splash.Controller splashController;
    private VBox menuRoot;
    private com.SpriteTool.PopMenu.Controller menuController;
    private Parent mainRoot;
    private com.SpriteTool.Controller mainController;

    public void go(String[] args) {
        launch(args);
    }

    public void start() {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(getSplashRoot(), 675, 400));
        primaryStage.show();
    }

    private void initLoaders() {
        try {
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Splash/Splash.fxml"));
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("PopMenu/PopMenu.fxml"));
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("SpriteTool.fxml"));
            this.splashRoot = splashLoader.load();
            this.menuRoot = menuLoader.load();
            this.mainRoot = mainLoader.load();
            this.menuRoot.setStyle("-fx-background-color: #3C3C3C");
            this.splashController = splashLoader.getController();
            this.menuController = menuLoader.getController();
            this.mainController = mainLoader.getController();
            this.splashController.setSpriteTool(this);
            this.menuController.setSpriteTool(this);
            this.mainController.setSpriteTool(this);
        } catch (IOException a) {
            a.printStackTrace();
        }

    }

    public void closeSplash() {
        Stage newStage = new Stage();
        newStage.setTitle("OpenRSC Sprite Tool");

        Scene scene = new Scene(this.mainRoot, 800, 500);
        newStage.setScene(scene);

        newStage.show();
        primaryStage.hide();

        setPrimaryStage(newStage);
    }

    public void loadWorkspace() {
        getMainController().closeDrawer();

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

        WorkspaceReader reader = new WorkspaceReader();
        this.workspace = reader.loadWorkspace(selectedDirectory.toPath());

        if (this.workspace == null) {
            System.out.print("Failed to load workspace.");
            return;
        }

        //Display the workspace
        mainController.populateSubspaceList(this.workspace);
    }

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }
    public Stage getPrimaryStage() { return this.primaryStage; }

    public Workspace getWorkspace() { return this.workspace; }
    public Parent getSplashRoot() { return this.splashRoot; }
    public Parent getMainRoot() { return this.mainRoot; }
    public VBox getMenuRoot() { return this.menuRoot; }
    public com.SpriteTool.Splash.Controller getSplashController() { return this.splashController; }
    public com.SpriteTool.PopMenu.Controller getMenuController() { return this.menuController; }
    public com.SpriteTool.Controller getMainController() { return this.mainController; }
}
