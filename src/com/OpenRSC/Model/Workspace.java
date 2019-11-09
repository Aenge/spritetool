package com.OpenRSC.Model;

import com.OpenRSC.IO.Workspace.WorkspaceWriter;
import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Workspace {

    private Path home;
    private String name;

    private List<Subspace> subspaces = new ArrayList<Subspace>();
    private Subspace activeSubspace;

    public Workspace(Path directory) {
        this.home = directory;
        this.name = directory.getFileName().toString();
    }

    public Path getHome() { return this.home; }
    public String getName() { return this.name; }

    public List<Subspace> getSubspaces() {
        return this.subspaces;
    }

    public Subspace getActiveSubspace() { return this.activeSubspace; }

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
            ss = new Subspace(Paths.get(home.toString(),name));
        } catch (InvalidPathException a) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setHeaderText("Invalid category name.");
            invalid.showAndWait();
            return false;
        }


        WorkspaceWriter wswriter = new WorkspaceWriter(this);
        if (wswriter.createSubspace(ss)) {
            subspaces.add(ss);
            return true;
        }

        return false;
    }

}

