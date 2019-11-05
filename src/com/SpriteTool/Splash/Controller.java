package com.SpriteTool.Splash;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(1);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Stage primaryStage = (Stage) rootPane.getScene().getWindow();
                            Stage newStage = new Stage();

                            URL url = new File("src/com/SpriteTool/SpriteTool.fxml").toURI().toURL();
                            Parent root = FXMLLoader.load(url);

                            newStage.setTitle("OpenRSC Sprite Tool");
                            newStage.setScene(new Scene(root, 600, 600));

                            while (primaryStage.getOpacity() > 0.1) {
                                Thread.sleep(75);
                                primaryStage.setOpacity(primaryStage.getOpacity() - 0.1);
                            }

                            newStage.show();
                            primaryStage.hide();

                        } catch (IOException a) {
                            a.printStackTrace();
                        } catch (InterruptedException b) {
                            b.printStackTrace();
                        }

                    }
                });
            } catch (InterruptedException a) {
                a.printStackTrace();
            }
        }

    }

}
