package com.OpenRSC.Model.Format;

import com.OpenRSC.IO.Archive.ImageData;
import com.OpenRSC.IO.ImageReader;

import java.io.File;

public class Sprite {

    private String name;
    private Info info;
    private ImageData data;

    public Sprite(File imageFile, Info info) {
        this.name = imageFile.getName();
        this.info = info;
        this.loadImageData(imageFile);
    }

    public String getName() { return this.name; }

    public void loadImageData(File file) {
        ImageReader imageReader = new ImageReader();
        this.data  = imageReader.read(file);
    }

    public Info getInfo() { return this.info; }
    public ImageData getData() { return this.data; }
}
