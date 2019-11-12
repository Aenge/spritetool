package com.OpenRSC.Model.Format;

public class ImageData {
    private int width;
    private int height;
    private int[] pixels;

    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height * width];
    }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int[] getPixels() { return this.pixels; }
}
