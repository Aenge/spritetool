package com.SpriteTool;

import com.SpriteTool.Model.Workspace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        SpriteTool spriteTool = new SpriteTool(primaryStage);
        spriteTool.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
