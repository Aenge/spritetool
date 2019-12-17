package com.OpenRSC.IO.Archive;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class Packer {

    public Packer() {
    }

    public boolean packArchive(Workspace workspace, File file) {
        try {

            long size = workspace.getSizeInBytes();
            ByteBuffer buffer = ByteBuffer.allocate((int)size);
            //Write the number of subspaces
            buffer.put((byte)workspace.getSubspaces().size());
            for (Subspace subspace : workspace.getSubspaces()) {
                //Write the subspace name
                writeString(buffer, subspace.getName());
                //Write the number of entries
                buffer.putShort((short)subspace.getEntryList().size());
                for (Entry entry : subspace.getEntryList()) {
                    writeString(buffer, entry.getID());
                    writeEntry(buffer, entry);
                }
            }
            // create file output stream
            FileOutputStream fOS = new FileOutputStream(file);

            // create zip output stream
            GZIPOutputStream gOS = new GZIPOutputStream(fOS);

            buffer.rewind();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            gOS.write(bytes);
            gOS.close();
            fOS.close();
        } catch (Exception a) { a.printStackTrace(); return false; }

        return true;
    }

    public boolean packEntry(Entry entry, File file) {

        try {
            // create file output stream
            FileOutputStream fOS = new FileOutputStream(file);

            // create zip output stream
            GZIPOutputStream gOS = new GZIPOutputStream(fOS);

            DataOutputStream dOS = new DataOutputStream(gOS);

            writeEntry(dOS, entry);

            dOS.close();
            gOS.close();
            fOS.close();
        } catch (Exception a) {
            a.printStackTrace();
            return false;
        }

        return true;
    }

    private void writeString(ByteBuffer stream, String string) {
        try {
            for (int i = 0; i < string.length(); ++i)
                stream.put((byte) string.charAt(i));
            stream.put((byte)0);
        } catch (Exception a) { a.printStackTrace(); }
    }
    private void writeEntry(DataOutputStream stream, Entry entry) {
        try {
            //Write the entry type
            stream.writeByte((byte) (entry.getType().ordinal() & 0xFF));

            //Write the entry layer
            if (entry.getLayer() != null)
                stream.writeByte((byte) (entry.getLayer().ordinal() & 0xFF));

            //Write the framecount
            stream.writeByte((byte) (entry.getFrames().length & 0xFF));

            //Generate color table
            ArrayList<Integer> colors = entry.getUniqueColors();

            //Write the amount of colors in the color table
            stream.writeByte((colors.size() - 1) & 0xFF);

            //Write the color table
            for (int color : colors) {
                byte red = (byte) ((color >> 16) & 0xFF);
                byte green = (byte) ((color >> 8) & 0xFF);
                byte blue = (byte) (color & 0xFF);
                stream.writeByte(red);
                stream.writeByte(green);
                stream.writeByte(blue);
            }

            //Write the frames
            for (Frame frame : entry.getFrames()) {
                stream.writeShort((short) (frame.getWidth() & 0xFFFF));
                stream.writeShort((short) (frame.getHeight() & 0xFFFF));

                stream.writeByte((byte) (frame.getUseShift() ? 1 : 0));
                stream.writeShort((short) (frame.getOffsetX() & 0xFFFF));
                stream.writeShort((short) (frame.getOffsetY() & 0xFFFF));
                stream.writeShort((short) (frame.getBoundWidth() & 0xFFFF));
                stream.writeShort((short) (frame.getBoundHeight() & 0xFFFF));

                for (int pixel : frame.getPixels()) {
                    int index = colors.indexOf(pixel);
                    stream.writeByte((byte) (index & 0xFF));
                }
            }
        } catch (Exception a) { a.printStackTrace(); }
    }
    private void writeEntry(ByteBuffer stream, Entry entry) {
        try {
            //Write the entry type
            stream.put((byte) (entry.getType().ordinal() & 0xFF));

            //Write the entry layer
            if (entry.getLayer() != null)
                stream.put((byte) (entry.getLayer().ordinal() & 0xFF));

            //Write the framecount
            stream.put((byte) (entry.getFrames().length & 0xFF));

            //Generate color table
            ArrayList<Integer> colors = entry.getUniqueColors();

            //Write the amount of colors in the color table
            stream.put((byte)((colors.size() - 1) & 0xFF));

            //Write the color table
            for (int color : colors) {
                byte red = (byte) ((color >> 16) & 0xFF);
                byte green = (byte) ((color >> 8) & 0xFF);
                byte blue = (byte) (color & 0xFF);
                stream.put(red);
                stream.put(green);
                stream.put(blue);
            }

            //Write the frames
            for (Frame frame : entry.getFrames()) {
                stream.putShort((short) (frame.getWidth() & 0xFFFF));
                stream.putShort((short) (frame.getHeight() & 0xFFFF));

                stream.put((byte) (frame.getUseShift() ? 1 : 0));
                stream.putShort((short) (frame.getOffsetX() & 0xFFFF));
                stream.putShort((short) (frame.getOffsetY() & 0xFFFF));
                stream.putShort((short) (frame.getBoundWidth() & 0xFFFF));
                stream.putShort((short) (frame.getBoundHeight() & 0xFFFF));

                for (int pixel : frame.getPixels()) {
                    int index = colors.indexOf(pixel);
                    stream.put((byte) (index & 0xFF));
                }
            }
        } catch (Exception a) { a.printStackTrace(); }
    }
}
