package com.SpriteTool.Model;

import java.nio.file.Path;

public class Workspace {

    private Path home;
    private String name = "cheeks mcgee";

    public Workspace(Path directory) {
        home = directory;
    }

    public Path getHome() { return this.home; }
    public String getName() { return this.name; }
}

