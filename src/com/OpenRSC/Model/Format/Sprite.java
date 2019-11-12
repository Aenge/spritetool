package com.OpenRSC.Model.Format;

import com.OpenRSC.IO.Image.ImageReader;
import java.io.File;

public class Sprite {

    private Info info;
    private ImageData imageData;

    public Sprite(File imageFile, Info info) {
        this.info = info;
        this.loadImageData(imageFile);
    }

    Sprite() {}

    @Override
    public boolean equals(Object o) {
        return true;
    }

    private void setInfo(Info info) { this.info = info; }
    private void setImageData(ImageData data) { this.imageData = data; }
    public String getName() { return this.getInfo().getName(); }

    public void loadImageData(File file) {
        ImageReader imageReader = new ImageReader();
        this.imageData  = imageReader.read(file);
    }

    public Info getInfo() { return this.info; }
    public ImageData getImageData() { return this.imageData; }

    public Sprite clone() {
        Sprite sprite = new Sprite();
        sprite.setInfo(this.getInfo().clone());
        sprite.setImageData(this.getImageData().clone());
        return sprite;
    }
}
