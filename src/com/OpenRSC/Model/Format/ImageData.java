package com.OpenRSC.Model.Format;

import javafx.scene.image.Image;

import java.util.Arrays;

public class ImageData {
    private int width;
    private int height;
    private int[] pixels;

    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height * width];
    }

    ImageData() {}

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!ImageData.class.isAssignableFrom(o.getClass()))
            return false;

        ImageData imageData = (ImageData)o;

        if (this.width != imageData.width ||
            this.height != imageData.height )
            return false;

        for (int i = 0; i < pixels.length; ++i) {
            if (this.pixels[i] != imageData.pixels[i])
                return false;
        }

        return true;
    }
    private void setWidth(int width) { this.width = width; }
    private void setHeight(int height) { this.height = height; }
    private void setPixels(int[] pixels) { this.pixels = Arrays.copyOf(pixels, pixels.length); }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int[] getPixels() { return this.pixels; }

    public ImageData clone() {
        ImageData imageData = new ImageData();

        imageData.setWidth(getWidth());
        imageData.setHeight(getHeight());
        imageData.setPixels(pixels);

        return imageData;
    }
}
