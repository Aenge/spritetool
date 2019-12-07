package com.OpenRSC.IO.Image;

import com.OpenRSC.Model.Frame;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    public ImageWriter() { }

    public boolean write(File file, Frame frame) {

        if (file == null ||
                frame == null)
            return true;

        File parentDir = file.getParentFile();
        if (!parentDir.exists())
            parentDir.mkdir();

        if (file.exists())
            file.delete();

        int[] giveAlpha = new int[frame.getPixels().length];
        for (int i=0; i<frame.getPixels().length; ++i)
            giveAlpha[i] = 0xFF000000 | frame.getPixels()[i];
        WritableImage writableImage = new WritableImage(frame.getWidth(), frame.getHeight());
        PixelWriter pw = writableImage.getPixelWriter();
        pw.setPixels(0, 0, frame.getWidth(), frame.getHeight(), PixelFormat.getIntArgbInstance(), giveAlpha, 0, frame.getWidth());

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException a) {
            a.printStackTrace();
            return false;
        }

       return true;
    }
}
