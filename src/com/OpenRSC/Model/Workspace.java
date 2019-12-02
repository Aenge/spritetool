package com.OpenRSC.Model;

import com.OpenRSC.IO.Workspace.WorkspaceWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public class Workspace {

    private Path home;
    private String name;
    private transient ObservableList<Subspace> subspaces = FXCollections.observableArrayList(Subspace.extractor());

    public Workspace(Path home) {
        this.home = home;
        this.name = home.getFileName().toString();
    }

    public String getName() { return this.name; }
    public Path getHome() { return this.home; }
    public ObservableList<Subspace> getSubspaces() {
        return this.subspaces;
    }

    public Subspace getSubspaceByName(String name) {
        for (Subspace subspace : getSubspaces()) {
            if (subspace.getName().equalsIgnoreCase(name))
                return subspace;
        }

        return null;
    }

    public int getSubspaceCount() { return this.subspaces.size(); }
    public int getEntryCount() {
        int entryCount = 0;
        for (Subspace subspace : this.subspaces) {
            entryCount += subspace.getEntryCount();
        }
        return entryCount;
    }

    public int getSpriteCount() {
        int spriteCount = 0;
        for (Subspace subspace : this.subspaces) {
            spriteCount += subspace.getSpriteCount();
        }
        return spriteCount;
    }
    public int getAnimationCount() {
        int animationCount = 0;
        for (Subspace subspace : this.subspaces) {
            animationCount += subspace.getAnimationCount();
        }
        return animationCount;
    }

    public boolean createSubspace(String name) {

        if (home == null)
            return false;

        Subspace ss;
        try {
            ss = new Subspace(name, home);
        } catch (InvalidPathException a) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setHeaderText("Invalid category name.");
            invalid.showAndWait();
            return false;
        }


        WorkspaceWriter wswriter = new WorkspaceWriter(this.home);
        if (wswriter.createSubspace(ss)) {
            subspaces.add(ss);
            return true;
        }

        return false;
    }

    public boolean deleteSubspace(Subspace ss) {
        if (!subspaces.contains(ss))
            return false;

        subspaces.remove(ss);
        try {
            FileUtils.deleteDirectory(new File(home.toString(), ss.getName()));
        } catch (IOException a) {
            a.printStackTrace();
            return false;
        }

        return true;
    }
}

