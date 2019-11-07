package com.OpenRSC.Interface.Splash;

import com.OpenRSC.SpriteTool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private SpriteTool spriteTool;

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
                Thread.sleep(3000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Stage primaryStage = spriteTool.getPrimaryStage();

                            while (primaryStage.getOpacity() > 0.1) {
                                Thread.sleep(75);
                                primaryStage.setOpacity(primaryStage.getOpacity() - 0.1);
                            }

                            spriteTool.closeSplash();
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

    public void setSpriteTool(SpriteTool ref) { this.spriteTool = ref; }
}
