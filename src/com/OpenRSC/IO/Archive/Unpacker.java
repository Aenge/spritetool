package com.OpenRSC.IO.Archive;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Entry.TYPE;
import com.OpenRSC.Model.Format.Frame;
import com.OpenRSC.Model.Format.ImageData;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Render.PlayerRenderer;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class Unpacker {

    public Unpacker() {

    }

    public Entry unpack(File file) {
        if (!file.exists())
            return null;

        byte[] bytesArray = new byte[(int) file.length()];
        Entry newEntry = new Entry();
        try {
            FileInputStream fis = new FileInputStream(file);
            GZIPInputStream gIS = new GZIPInputStream(fis);
            DataInputStream input = new DataInputStream(gIS);


            newEntry.setID(file.getName());

            newEntry.setType(TYPE.get(input.readByte()));
            if (newEntry.getType().getLayers().length != 0)
                newEntry.setLayer(PlayerRenderer.LAYER.get(input.readByte()));

            int frameCount = input.readByte();

            int[] colorTable = new int[input.readByte()];

            for (int i=0; i < colorTable.length; ++i) {
                int Red = input.readByte() & 0xFF;
                int Green = input.readByte() & 0xFF;
                int Blue = input.readByte() & 0xFF;
                colorTable[i] = Red << 16 | Green << 8 | Blue;
            }

            for (int i=0; i < frameCount; ++i) {
                Frame newSprite = new Frame();
                Info info = new Info();
                ImageData imageData = new ImageData();
                imageData.width = input.readByte();
                imageData.height = input.readByte();
                info.useShift = input.readByte() == 1;
                info.offsetX = input.readByte();
                info.offsetY = input.readByte();
                info.boundwidth = input.readByte();
                info.boundheight = input.readByte();
                info.setFrameCount(frameCount);
                info.setFrame(i+1);
                info.setType(newEntry.getType());
                info.setLayer(newEntry.getLayer());
                info.setID(newEntry.getID());
                imageData.pixels = new int[imageData.width * imageData.height];
                for (int p=0; p < imageData.width * imageData.height; ++p)
                    imageData.pixels[p] = colorTable[input.readByte()];

                newSprite.info = info;
                newSprite.imageData = imageData;
                newEntry.addFrame(newSprite);
            }

        } catch (IOException a) { a.printStackTrace(); return null; }

        return newEntry;
    }
}
