package com.SpriteTool;

import com.SpriteTool.Model.Subspace;
import com.SpriteTool.Model.Workspace;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class SpriteTool {

    private static final Logger LOGGER = LogManager.getLogger();

    private Stage primaryStage;
    private Workspace workspace;

    private Parent splashRoot;
    private com.SpriteTool.Splash.Controller splashController;
    private GridPane menuRoot;
    private com.SpriteTool.PopMenu.Controller menuController;
    private Parent mainRoot;
    private com.SpriteTool.Controller mainController;


    public SpriteTool(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initLoaders();
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
            this.splashController = splashLoader.getController();
            this.menuController = menuLoader.getController();
            this.mainController = mainLoader.getController();
            this.splashController.setSpriteTool(this);
            this.menuController.setSpriteTool(this);
            this.mainController.setSpriteTool(this);
        } catch (IOException a) {
            LOGGER.catching(a);
        }

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
            LOGGER.info("Invalid directory chosen");
            return;
        }

        this.workspace = new Workspace(selectedDirectory.toPath());
        displayWorkspace();
    }

    private void displayWorkspace() {
        if (this.workspace == null) {
            LOGGER.info("Tried to display a null workspace.");
            return;
        }

        for (Subspace subspace : this.workspace.getSubspaces()) {
            mainController.listSubspace(subspace);
        }
    }

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }
    public Stage getPrimaryStage() { return this.primaryStage; }

    public Workspace getWorkspace() { return this.workspace; }
    public Parent getSplashRoot() { return this.splashRoot; }
    public Parent getMainRoot() { return this.mainRoot; }
    public GridPane getMenuRoot() { return this.menuRoot; }
    public com.SpriteTool.Splash.Controller getSplashController() { return this.splashController; }
    public com.SpriteTool.PopMenu.Controller getMenuController() { return this.menuController; }
    public com.SpriteTool.Controller getMainController() { return this.mainController; }
}
