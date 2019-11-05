package com.SpriteTool;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class SpriteTool {

    private String name = "Boogers";
    public Stage primaryStage;

    public Parent splashRoot;
    public com.SpriteTool.Splash.Controller splashController;
    public GridPane menuRoot;
    public com.SpriteTool.PopMenu.Controller menuController;
    public Parent mainRoot;
    public com.SpriteTool.Controller mainController;

    public SpriteTool(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initLoaders();
    }

    public void start() {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(this.splashRoot, 675, 400));
        primaryStage.show();
    }

    public void initLoaders() {
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
            a.printStackTrace();
        }

    }

    public String getName() { return this.name; }

    public void loadWorkspace() {
        System.out.print(this.getName());
    }

    public void setPrimaryStage(Stage stage) { this.primaryStage = stage; }
}
