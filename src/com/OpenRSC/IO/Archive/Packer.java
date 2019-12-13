package com.OpenRSC.IO.Archive;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class Packer {

    public Packer() {
    }

    public boolean packArchive(Workspace workspace, File file) {
        try {
            // create file output stream
            FileOutputStream fOS = new FileOutputStream(file);

            // create zip output stream
            GZIPOutputStream gOS = new GZIPOutputStream(fOS);

            DataOutputStream dOS = new DataOutputStream(gOS);

            //Write the number of subspaces
            dOS.writeByte(workspace.getSubspaces().size());

            for (Subspace subspace : workspace.getSubspaces()) {
                //Write the subspace name
                writeString(dOS, subspace.getName());
                //Write the number of entries
                dOS.writeShort(subspace.getEntryList().size());
                for (Entry entry : subspace.getEntryList()) {
                    writeString(dOS, entry.getID());
                    writeEntry(dOS, entry);
                }
            }

            dOS.close();
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

    private void writeString(DataOutputStream stream, String string) {
        try {
            for (int i = 0; i < string.length(); ++i)
                stream.writeByte((byte) string.charAt(i));
            stream.writeByte(0);
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
}
