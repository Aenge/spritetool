package com.OpenRSC.IO.Info;

import com.OpenRSC.Model.Format.Info;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;

public class InfoWriter {

    File file;
    Info info;

    public InfoWriter(File infoFile, Info info) {
        this.file = infoFile;
        this.info = info;
        if (!infoFile.canWrite())
            this.file = null;
    }

    public void write() {
        if (this.file == null || this.info == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Unable to write to info file.");
            error.showAndWait();
            return;
        }

        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(this.file, this.info);
        } catch (IOException a) {
            a.printStackTrace();
        }
    }
}
