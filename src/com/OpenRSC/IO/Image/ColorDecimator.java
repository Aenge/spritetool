package com.OpenRSC.IO.Image;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColorDecimator {

    private Map<Integer, Integer> decimatedColors;

    public int[] decimatePixelColors(int[] originalPixels) {
        if (originalPixels == null ||
        decimatedColors == null)
            return null;

        for (int i=0; i<originalPixels.length; ++i) {
            int value = originalPixels[i];
            while (decimatedColors.containsKey(value))
                value = decimatedColors.get(value);
            originalPixels[i]=value;
        }

        return originalPixels;
    }

    public void reduceColorPalette(ArrayList<Integer> originalColors, int maxColors, SimpleDoubleProperty progress) {
        Map<Integer, Integer> decimatedColors = new HashMap<>();
        double colorOverhead = originalColors.size() - 255;
        while(originalColors.size() > maxColors) {
            Match match = findMatch(originalColors);

            originalColors.remove(Integer.valueOf(match.color2));
            decimatedColors.put(match.color2, match.color1);
            progress.set((double)decimatedColors.size() / colorOverhead);
        }

        this.decimatedColors = decimatedColors;
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

    private class Match{
        public int distance;
        public int color1;
        public int color2;
    }
}
