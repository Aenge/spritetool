package com.OpenRSC.Model.Format;

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
