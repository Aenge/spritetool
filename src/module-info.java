module spritetool {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires apachecommons;
    requires java.desktop;
    requires javafx.swing;
    requires org.controlsfx.controls;
    opens com.OpenRSC.Render;
    opens com.OpenRSC.Model;
    opens com.OpenRSC.Model.Format;
    opens com.OpenRSC.Interface.Splash;
    opens com.OpenRSC.Interface.SpriteTool;
    opens com.OpenRSC.Interface.CreateWorkspace;
    opens com.OpenRSC;
}