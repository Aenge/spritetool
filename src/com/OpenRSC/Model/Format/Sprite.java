package com.OpenRSC.Model.Format;

import com.OpenRSC.IO.Archive.ImageData;
import com.OpenRSC.IO.Image.ImageReader;

import java.io.File;
import java.util.regex.Pattern;

public class Sprite {

    private String name;
    private Info info;
    private ImageData imageData;

    public Sprite(File imageFile, Info info) {
        String name = imageFile.getName();
        int findPeriod = name.lastIndexOf(".");
        if (findPeriod != -1)
            name = name.substring(0, findPeriod);
        this.name = name;
        this.info = info;
        this.loadImageData(imageFile);
    }

    public String getName() { return this.name; }

    public void loadImageData(File file) {
        ImageReader imageReader = new ImageReader();
        this.imageData  = imageReader.read(file);
    }

    public Info getInfo() { return this.info; }
    public ImageData getImageData() { return this.imageData; }
}
