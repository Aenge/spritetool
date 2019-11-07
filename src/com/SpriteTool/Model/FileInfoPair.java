package com.SpriteTool.Model;

import com.SpriteTool.IO.InfoReader;

import java.io.File;
import java.nio.file.Path;

public class FileInfoPair {
    private Path path;
    private String name;

    private Entry.TYPE type;
    private int entryID;
    private int frameCount;
    private int frame;
    private int offsetX;
    private int offsetY;
    private int boundwidth;
    private int boundheight;

    public FileInfoPair(Path path, String name) {
        this.path = path;
        this.name = name;
        InfoReader.read(this);
        System.out.print("EntryID: " + entryID + "\nType: " + type.getValue());
    }

    public Path getPath() { return this.path; }
    public String getName() { return this.name; }
    public File getInfoFile() { return new File(path.toString(),name + ".info"); }
    public File getPngFile() { return new File(path.toString(),name + ".png"); }
    public Entry.TYPE getType() { return this.type; }
    public void setType(Entry.TYPE type) { this.type = type; }
    public void setEntryID(int id) { this.entryID = id; }
    public void setFrame(int frame) { this.frame = frame; }
    public void setFrameCount(int framecount) { this.frameCount = framecount; }
    public void setOffsetX(int value) { this.offsetX = value; }
    public void setOffsetY(int value) { this.offsetY = value; }
    public void setBoundWidth(int value) { this.boundwidth = value; }
    public void setBoundHeight(int value) { this.boundheight = value; }
}
