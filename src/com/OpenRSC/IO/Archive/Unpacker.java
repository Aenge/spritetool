package com.OpenRSC.IO.Archive;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Entry.TYPE;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.OpenRSC.Render.PlayerRenderer.LAYER;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileLockInterruptionException;
import java.util.zip.GZIPInputStream;

public class Unpacker {

    public Unpacker() {

    }

    public Workspace unpackArchive(File file) {
        if (!file.exists())
            return null;

        try {
            int index = file.getName().lastIndexOf('.');
            String name = file.getName().substring(0, index );
            Workspace newWorkspace = new Workspace(new File("C:/temp/brobeans").toPath());
            newWorkspace.changeName(name);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                InputStream in = new GZIPInputStream(new FileInputStream(file));
                byte[] buffer = new byte[65536];
                int noRead;
                while ((noRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, noRead);
                }
            } finally {
                try { out.close(); } catch (Exception e) {}
            }
            ByteBuffer input = ByteBuffer.wrap(out.toByteArray());
            
            int subspaceCount = ((int) input.get()) & 0xFF;

            for (int i = 0; i < subspaceCount; ++i) {
                String subspaceName = readString(input);
                File subspaceHome = new File(newWorkspace.getHome().toString(), subspaceName);

                Subspace newSubspace = new Subspace(subspaceName, subspaceHome.toPath());
                readSubspace(input, newSubspace);
                newWorkspace.getSubspaces().add(newSubspace);
            }

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
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                GZIPInputStream in = new GZIPInputStream(fis);
                byte[] buffer = new byte[65536];
                int noRead;
                while ((noRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, noRead);
                }

                in.close();
            } finally {
                try { out.close(); fis.close();} catch (Exception e) {}
            }

            ByteBuffer input = ByteBuffer.wrap(out.toByteArray());
            TYPE type;

            Entry newEntry = new Entry(
                    FilenameUtils.removeExtension(file.getName()),
                    type = TYPE.get((int) input.get() & 0xFF),
                    type.getLayers().length == 0 ? null : LAYER.get((int) input.get() & 0xFF),
                    (int) input.get() & 0xFF
            );

            readEntry(input, newEntry);

            return newEntry;

        } catch (IOException a) {
            a.printStackTrace();
            return null;
        }
    }

    private void readSubspace(ByteBuffer stream, Subspace subspace) {
        try {
            int numEntries = ((int)stream.getShort()) & 0xFFFF;

            for (int i=0; i<numEntries; ++i) {
                String entryName = readString(stream);
                TYPE type;
                Entry newEntry = new Entry(
                        entryName,
                        type = TYPE.get(((int) stream.get()) & 0xFF),
                        type.getLayers().length == 0 ? null : LAYER.get(((int) stream.get()) & 0xFF),
                        ((int) stream.get()) & 0xFF
                );

                readEntry(stream, newEntry);
                subspace.getEntryList().add(newEntry);
            }

        } catch (Exception a) { a.printStackTrace(); }
    }

    private void readEntry(ByteBuffer stream, Entry entry) {
        try {
            int tableSize = stream.get() & 0xFF;
            int[] colorTable = new int[++tableSize];

            for (int i = 0; i < colorTable.length; ++i) {
                int Red = stream.get() & 0xFF;
                int Green = stream.get() & 0xFF;
                int Blue = stream.get() & 0xFF;
                colorTable[i] = Red << 16 | Green << 8 | Blue;
            }

            for (int i = 0; i < entry.getFrames().length; ++i) {
                Frame frame = new Frame(
                        (int) stream.getShort() & 0xFFFF,
                        (int) stream.getShort() & 0xFFFF,
                        stream.get() == 1,
                        (int) stream.getShort(),
                        (int) stream.getShort(),
                        (int) stream.getShort() & 0xFFFF,
                        (int) stream.getShort() & 0xFFFF
                );

                for (int p = 0; p < frame.getPixels().length; ++p)
                    frame.getPixels()[p] = colorTable[(int) stream.get() & 0xFF];

                entry.getFrames()[i] = frame;
            }
        } catch (Exception a) { a.printStackTrace(); }
    }

    private String readString(ByteBuffer stream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int character;
            while ((character = stream.get()) != 0)
                stringBuilder.append((char)(character & 0xFF));
        } catch (Exception a) {
            a.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
