package com.SpriteTool;
import javafx.application.Application;
import javafx.stage.Stage;

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
