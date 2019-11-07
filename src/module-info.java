module OpenRSC.Sprite.Tool {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires fontawesomefx;
    requires log4j.api;
    requires log4j.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    exports com.SpriteTool.PopMenu;
    opens com.SpriteTool.PopMenu;
    opens com.SpriteTool.Splash;
    opens com.SpriteTool;
}