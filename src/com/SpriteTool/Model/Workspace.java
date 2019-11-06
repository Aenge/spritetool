package com.SpriteTool.Model;

import com.SpriteTool.IO.WorkspaceReader;
import java.nio.file.Path;
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
        WorkspaceReader.loadWS(this);
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
}

