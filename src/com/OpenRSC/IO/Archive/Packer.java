package com.OpenRSC.IO.Archive;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Frame;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class Packer {

    Entry entry;

    public Packer(Entry entry) {
        this.entry = entry;
    }

    public void pack(File file) {

        try {
            // create file output stream
            FileOutputStream fOS = new FileOutputStream(file);

            // create zip output stream
            GZIPOutputStream gOS = new GZIPOutputStream(fOS);

            DataOutputStream dOS = new DataOutputStream(gOS);
            //Write the entry type
            dOS.writeByte((byte)(entry.getType().ordinal() & 0xFF));

            //Write the entry layer
            if (entry.getLayer() != null)
                dOS.writeByte((byte)(entry.getLayer().ordinal() & 0xFF));

            //Write the framecount
            dOS.writeByte((byte)(entry.getFrames().size() & 0xFF));

            //Generate color table
            ArrayList<Integer> colors = new ArrayList<>();

            for (int i=0; i < this.entry.getFrames().size(); ++i) {
                for (int color : this.entry.getFrame(i).getImageData().getPixels()) {
                    if (!colors.contains(color))
                        colors.add(color);
                }
            }

            //Write the amount of colors in the color table
            dOS.writeShort((short)(colors.size() & 0xFFFF));

            //Write the color table
            for (int color : colors) {
                byte red = (byte)((color >> 16) & 0xFF);
                byte green = (byte)((color >> 8) & 0xFF);
                byte blue = (byte)(color & 0xFF);
                dOS.writeByte(red);
                dOS.writeByte(green);
                dOS.writeByte(blue);
            }

            //Write the frames
            for (Frame sprite : entry.getFrames()) {
                dOS.writeShort((short) (sprite.getImageData().getWidth() & 0xFFFF));
                dOS.writeShort((short) (sprite.getImageData().getHeight() & 0xFFFF));

                dOS.writeByte((byte)  (sprite.getInfo().useShift ? 1 : 0));
                dOS.writeShort((short) (sprite.getInfo().getOffsetX() & 0xFFFF));
                dOS.writeShort((short) (sprite.getInfo().getOffsetY() & 0xFFFF));
                dOS.writeShort((short) (sprite.getInfo().getBoundWidth() & 0xFFFF));
                dOS.writeShort((short) (sprite.getInfo().getBoundHeight() & 0xFFFF));

                for (int pixel : sprite.getImageData().getPixels()) {
                    int index = colors.indexOf(pixel);
                    dOS.writeByte((byte)(index & 0xFF));
                }
            }

            dOS.close();
            fOS.close();

        } catch (IOException a) { a.printStackTrace(); return; }
    }
}
