module OpenRSC.Sprite.Tool {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires fontawesomefx;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    opens com.SpriteTool.PopMenu;
    opens com.SpriteTool.Splash;
    opens com.SpriteTool;
}