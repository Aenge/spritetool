package com.OpenRSC.IO.Image;

import com.OpenRSC.Model.Format.ImageData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    private File file;
    private ImageData imageData;

    public ImageWriter(File file, ImageData imageData) {
        this.file = file;
        this.imageData = imageData;
    }

    public boolean write() {
        if (this.file == null ||
                this.imageData == null)
            return true;

        WritableImage writableImage = new WritableImage(this.imageData.getWidth(), this.imageData.getHeight());
        PixelWriter pw = writableImage.getPixelWriter();
        pw.setPixels(0, 0, this.imageData.getWidth(), this.imageData.getHeight(), PixelFormat.getIntArgbInstance(), this.imageData.getPixels(), 0, this.imageData.getWidth());

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException a) {
            a.printStackTrace();
            return false;
        }

        return true;
    }
}
