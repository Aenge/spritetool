package com.OpenRSC.IO.Archive;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Frame;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class Packer {

    Entry entry;

    public Packer(Entry entry) {
        this.entry = entry;
    }

    public boolean pack(File file) {

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
            dOS.writeByte((byte)(entry.getFrames().length & 0xFF));

            //Generate color table
            ArrayList<Integer> colors = new ArrayList<>();

            for (int i=0; i < this.entry.getFrames().length; ++i) {
                for (int color : this.entry.getFrames()[i].getPixels()) {
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
            for (Frame frame : entry.getFrames()) {
                dOS.writeShort((short) (frame.getWidth() & 0xFFFF));
                dOS.writeShort((short) (frame.getHeight() & 0xFFFF));

                dOS.writeByte((byte)  (frame.getUseShift()? 1 : 0));
                dOS.writeShort((short) (frame.getOffsetX() & 0xFFFF));
                dOS.writeShort((short) (frame.getOffsetY() & 0xFFFF));
                dOS.writeShort((short) (frame.getBoundWidth() & 0xFFFF));
                dOS.writeShort((short) (frame.getBoundHeight() & 0xFFFF));

                for (int pixel : frame.getPixels()) {
                    int index = colors.indexOf(pixel);
                    dOS.writeByte((byte)(index & 0xFF));
                }
            }

            dOS.close();
            fOS.close();

        } catch (Exception a) {
            a.printStackTrace();
            return false;
        }

        return true;
    }
}
