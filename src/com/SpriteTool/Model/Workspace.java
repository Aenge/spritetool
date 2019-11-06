package com.SpriteTool.Model;

import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Workspace {

    private Path home;
    private String name;

    List<Subspace> subspaces = new ArrayList<Subspace>();

    public Workspace(Path directory) {
        this.home = directory;
        this.name = directory.getFileName().toString();
        fillWorkspace();
    }

    private void fillWorkspace() {
        File[] directories = new File(getHome().toUri()).listFiles(File::isDirectory);

        if (directories == null || directories.length == 0) {
            new Alert(Alert.AlertType.ERROR, "The chosen directory is not a valid workspace.").showAndWait();
            return;
        }

        for (File dir : directories) {
            subspaces.add(new Subspace(dir.toPath()));
        }
    }


    public Path getHome() { return this.home; }
    public String getName() { return this.name; }

    public List<Subspace> getSubspaces() {
        return this.subspaces;
    }
}

