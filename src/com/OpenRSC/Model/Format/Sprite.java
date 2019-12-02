package com.OpenRSC.Model.Format;

import com.OpenRSC.IO.Image.ImageReader;
import com.OpenRSC.IO.Info.InfoReader;

import java.io.File;

public class Sprite {

    private Info info;
    private ImageData imageData;

    public Sprite(File imageFile, File infoFile) {
        this.loadInfo(infoFile);
        this.loadImageData(imageFile);
    }

    public Sprite(File imageFile, Info info) {
        this.info = info;
        this.loadImageData(imageFile);
    }
    Sprite() {}

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Sprite.class.isAssignableFrom(o.getClass()))
            return false;

        Sprite sprite = (Sprite)o;

        if (!this.getInfo().equals(sprite.getInfo()))
            return false;

        if (!this.getImageData().equals(sprite.getImageData()))
            return false;

        return true;
    }

    private void setInfo(Info info) { this.info = info; }
    private void setImageData(ImageData data) { this.imageData = data; }
    public String getFileName() {
        return String.join("_", this.info.getID(), String.valueOf(this.info.getFrame()));
    }
    public String getID() { return this.getInfo().getID(); }

    private void loadImageData(File file) {
        ImageReader imageReader = new ImageReader();
        this.imageData  = imageReader.read(file);
    }

    private void loadInfo(File file) {
        InfoReader infoReader = new InfoReader();
        this.info = infoReader.read(file);
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
