package com.OpenRSC.IO.Image;

import com.OpenRSC.Model.Format.ImageData;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import java.io.File;
import java.nio.IntBuffer;

public class ImageReader {

    public ImageData read(File file) {
        if (!file.exists()) {
            System.out.print("Tried to read an image that doesn't exist.");
            return null;
        }

        Image image = new Image(file.toURI().toString());
        ImageData imageData = new ImageData((int)image.getWidth(), (int)image.getHeight());
        PixelReader pixelReader = image.getPixelReader();

        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        pixelReader.getPixels(0, 0, (int)image.getWidth(), (int)image.getHeight(), pixelFormat, imageData.getPixels(), 0, (int)image.getWidth());

        for (int i = 0; i < imageData.getPixels().length; ++i)
            imageData.getPixels()[i] &= 0xFFFFFF;


        return imageData;
    }
}
