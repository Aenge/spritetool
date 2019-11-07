package com.SpriteTool.Model.Format;

import java.io.File;

public class Sprite {

    private String name;

    public Sprite(File imageFile, Info info) {
        this.name = imageFile.getName();
    }

    public String getName() { return this.name; }
}
