package com.OpenRSC.IO.Archive;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Entry.TYPE;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Render.PlayerRenderer.LAYER;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class Unpacker {

    public Unpacker() {

    }

    public Entry unpack(File file) {
        if (!file.exists())
            return null;

        Entry retVal = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            GZIPInputStream gIS = new GZIPInputStream(fis);
            DataInputStream input = new DataInputStream(gIS);

            TYPE type;

            Entry newEntry = new Entry(
                    FilenameUtils.removeExtension(file.getName()),
                    type = TYPE.get((int)input.readByte() & 0xFF),
                    type.getLayers().length == 0 ? null : LAYER.get((int)input.readByte() & 0xFF),
                    (int)input.readByte() & 0xFF
            );

            int tableSize = (int)input.readShort() & 0xFFFF;
            int[] colorTable = new int[tableSize];

            for (int i=0; i < colorTable.length; ++i) {
                int Red = input.readByte() & 0xFF;
                int Green = input.readByte() & 0xFF;
                int Blue = input.readByte() & 0xFF;
                colorTable[i] = Red << 16 | Green << 8 | Blue;
            }

            for (int i=0; i < newEntry.getFrames().length; ++i) {
                Frame frame = new Frame(
                        (int)input.readShort() & 0xFFFF,
                        (int)input.readShort() & 0xFFFF,
                        input.readByte() == 1,
                        (int)input.readShort(),
                        (int)input.readShort(),
                        (int)input.readShort() & 0xFFFF,
                        (int)input.readShort() & 0xFFFF
                        );

                for (int p=0; p < frame.getPixels().length; ++p)
                    frame.getPixels()[p] = colorTable[(int)input.readByte() & 0xFF];

                newEntry.getFrames()[i] = frame;
            }

            retVal = newEntry;
        } catch (IOException a) { a.printStackTrace(); return null; }

        return retVal;
    }
}
