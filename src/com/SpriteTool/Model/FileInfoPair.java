package com.SpriteTool.Model;

import java.nio.file.Path;

public class FileInfoPair {
    private Path imagePath;
    private Path infoPath;

    private String name;
    private Entry.TYPE type;
    private int entryID;
    private int frameCount;
    private int frame;
    private int offsetX;
    private int offsetY;
    private int boundwidth;
    private int boundheight;

    public String getName() { return this.name; }
}
