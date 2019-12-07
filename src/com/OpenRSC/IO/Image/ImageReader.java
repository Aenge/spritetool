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

        //Check size of color table
        if (colorTable.size() > 1) {
            this.pixels = decimateColors(this.pixels, colorTable, 2);
        }
    }

    public int[] getPixels() { return this.pixels; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    private int[] decimateColors(int[] originalPixels, ArrayList<Integer> originalColors, int maxColors) {
        Map<Integer, Integer> mergedColors = new HashMap<>();

        while(originalColors.size() > maxColors) {
            if (originalColors.size() == 1000)
                System.out.println("yo");
            Match match = findMatch(originalColors);

            originalColors.remove(Integer.valueOf(match.color2));
            mergedColors.put(match.color2, match.color1);
        }


        for (int i=0; i<originalPixels.length; ++i) {
            int value = originalPixels[i];
            while (mergedColors.containsKey(value))
                value=mergedColors.get(value);
            originalPixels[i]=value;
        }

        return originalPixels;
    }

    private int distance(int color1, int color2) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;
        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        return Math.abs(red1-red2) + Math.abs(green1-green2) + Math.abs(blue1-blue2);
        //return Math.sqrt(Math.pow(red1-red2,2) + Math.pow(green1-green2,2) + Math.pow(blue1-blue2,2));
    }

    private Match findMatch(ArrayList<Integer> colors) {
        Match match = new Match();
        match.distance = Integer.MAX_VALUE;

        for (int i=0; i<colors.size(); ++i) {
            for (int k=0; k<i; ++k) {
                int distance = distance(colors.get(i),colors.get(k));
                if (distance < match.distance) {
                    match.distance = distance;
                    match.color1 = colors.get(i);
                    match.color2 = colors.get(k);
                    if (distance == 1)
                        return match;
                }
            }
        }

        return match;
    }

    private int mergeColors(int color1, int color2) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;
        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int red = Math.abs((red1+red2)/2);
        int green = Math.abs((green1+green2)/2);
        int blue = Math.abs((blue1+blue2)/2);

        return red << 16 | green << 8 | blue;
    }

    private class Match{
        public int distance;
        public int color1;
        public int color2;
    }
}
