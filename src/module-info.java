module OpenRSC.Sprite.Tool {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    exports com.SpriteTool;
    opens com.SpriteTool.Splash;
    opens com.SpriteTool;
}