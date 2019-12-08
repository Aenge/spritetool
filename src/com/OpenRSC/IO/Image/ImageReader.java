package com.OpenRSC.IO.Image;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageReader {

    private int[] pixels;
    private int width;
    private int height;

    public void read(File file) {
        if (!file.exists()) {
            return;
        }

        Image image = new Image(file.toURI().toString());

        this.width = (int)image.getWidth();
        this.height = (int)image.getHeight();
        this.pixels = new int[width * height];

        PixelReader pixelReader = image.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, this.pixels, 0, width);

        ArrayList<Integer> colorTable = new ArrayList<>();
        for (int i = 0; i < this.pixels.length; ++i) {
            pixels[i] &= 0xFFFFFF;
            if (!colorTable.contains(pixels[i]))
                colorTable.add(pixels[i]);
        }
    }

    public int[] getPixels() { return this.pixels; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
}
