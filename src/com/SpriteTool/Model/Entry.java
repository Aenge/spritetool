package com.SpriteTool.Model;

import com.SpriteTool.Model.Format.Sprite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Entry {

    public enum TYPE {
        SPRITE,
        ANIMATION;
    }

    private TYPE type;
    private List<Sprite> spriteList = new ArrayList<Sprite>();
    private List<File> fileList;

    public Entry(File file) {
        this.type = TYPE.SPRITE;
        fileList = new ArrayList<File>();
        fileList.add(file);
    }

    public Entry(List<File> files) {
        this.type = TYPE.ANIMATION;
        fileList = files;
    }

    public boolean isAnimation() { return this.type == TYPE.ANIMATION; }
    public boolean isSprite() { return this.type == TYPE.SPRITE; }
    public List<Sprite> getSprites() { return this.spriteList; }
    public File getFile() { return fileList.get(0); }
}
