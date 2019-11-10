module OpenRSC.Sprite.Tool {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires fontawesomefx;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.io;
    opens com.OpenRSC.Interface.PopMenu;
    opens com.OpenRSC.Interface.Splash;
    opens com.OpenRSC.Interface.SpriteTool;
    opens com.OpenRSC;
}