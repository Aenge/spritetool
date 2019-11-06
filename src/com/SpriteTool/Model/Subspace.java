package com.SpriteTool.Model;

import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Path;

public class Subspace {

    private Path path;

    public Subspace(Path path) {
        this.path = path;
        fillSubspace();
    }

    public Path getPath() { return this.path; }
    public String getName() { return this.path.getFileName().toString(); }

    private void fillSubspace() {
        File[] files = new File(getPath().toUri()).listFiles(File::isFile);

        if (files == null) {
            return;
        }
    }
}
