package com.OpenRSC.IO.Archive;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Entry.TYPE;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.PlayerRenderer.LAYER;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class Unpacker {

    public Unpacker() {

    }

    public Workspace unpackArchive(File file) {
        if (!file.exists())
            return null;

        try {
            Workspace newWorkspace = new Workspace(new File("C:/temp/brobeans").toPath());
            newWorkspace.changeName(FilenameUtils.removeExtension(file.getName()));

            FileInputStream fIS = new FileInputStream(file);
            GZIPInputStream gIS = new GZIPInputStream(fIS);
            DataInputStream input = new DataInputStream(gIS);

            int subspaceCount = ((int) input.readByte()) & 0xFF;

            for (int i = 0; i < subspaceCount; ++i) {
                String subspaceName = readString(input);
                File subspaceHome = new File(newWorkspace.getHome().toString(), subspaceName);

                Subspace newSubspace = new Subspace(subspaceName, subspaceHome.toPath());
                readSubspace(input, newSubspace);
                newWorkspace.getSubspaces().add(newSubspace);
            }

            input.close();
            gIS.close();
            fIS.close();

            return newWorkspace;
        } catch (Exception a) {
            a.printStackTrace();
            return null;
        }
    }

    public Entry unpackEntry(File file) {
        if (!file.exists())
            return null;

        try {
            FileInputStream fis = new FileInputStream(file);
            GZIPInputStream gIS = new GZIPInputStream(fis);
            DataInputStream input = new DataInputStream(gIS);

            TYPE type;

            Entry newEntry = new Entry(
                    FilenameUtils.removeExtension(file.getName()),
                    type = TYPE.get((int) input.readByte() & 0xFF),
                    type.getLayers().length == 0 ? null : LAYER.get((int) input.readByte() & 0xFF),
                    (int) input.readByte() & 0xFF
            );

            readEntry(input, newEntry);

            return newEntry;

        } catch (IOException a) {
            a.printStackTrace();
            return null;
        }
    }

    private void readSubspace(DataInputStream stream, Subspace subspace) {
        try {
            int numEntries = ((int)stream.readShort()) & 0xFFFF;

            for (int i=0; i<numEntries; ++i) {
                String entryName = readString(stream);
                TYPE type;
                Entry newEntry = new Entry(
                        entryName,
                        type = TYPE.get(((int) stream.readByte()) & 0xFF),
                        type.getLayers().length == 0 ? null : LAYER.get(((int) stream.readByte()) & 0xFF),
                        ((int) stream.readByte()) & 0xFF
                );

                readEntry(stream, newEntry);
                subspace.getEntryList().add(newEntry);
            }

        } catch (Exception a) { a.printStackTrace(); }
    }

    private void readEntry(DataInputStream stream, Entry entry) {
        try {
            int tableSize = stream.readByte() & 0xFF;
            int[] colorTable = new int[++tableSize];

            for (int i = 0; i < colorTable.length; ++i) {
                int Red = stream.readByte() & 0xFF;
                int Green = stream.readByte() & 0xFF;
                int Blue = stream.readByte() & 0xFF;
                colorTable[i] = Red << 16 | Green << 8 | Blue;
            }

            for (int i = 0; i < entry.getFrames().length; ++i) {
                Frame frame = new Frame(
                        (int) stream.readShort() & 0xFFFF,
                        (int) stream.readShort() & 0xFFFF,
                        stream.readByte() == 1,
                        (int) stream.readShort(),
                        (int) stream.readShort(),
                        (int) stream.readShort() & 0xFFFF,
                        (int) stream.readShort() & 0xFFFF
                );

                for (int p = 0; p < frame.getPixels().length; ++p)
                    frame.getPixels()[p] = colorTable[(int) stream.readByte() & 0xFF];

                entry.getFrames()[i] = frame;
            }
        } catch (Exception a) { a.printStackTrace(); }
    }

    private String readString(DataInputStream stream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int character;
            while ((character = stream.readByte()) != 0)
                stringBuilder.append((char)(character & 0xFF));
        } catch (Exception a) {
            a.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
