package com.SpriteTool.IO;

import com.SpriteTool.Model.Entry;
import com.SpriteTool.Model.FileInfoPair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;

public class InfoReader {
    private static Logger LOGGER = LogManager.getLogger();

    public static void read(FileInfoPair pair) {
        File infoFile = pair.getInfoFile();
        try {
            if (infoFile.exists()) {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jo = objectMapper.readTree(infoFile);

                pair.setType(Entry.TYPE.get(jo.get("type").asInt()));
                pair.setEntryID(jo.get("entryID").asInt());
                pair.setFrame(jo.get("frame").asInt());
                pair.setFrameCount(jo.get("framecount").asInt());
                pair.setOffsetX(jo.get("offsetX").asInt());
                pair.setOffsetY(jo.get("offsetY").asInt());
                pair.setBoundWidth(jo.get("boundwidth").asInt());
                pair.setBoundHeight(jo.get("boundheight").asInt());
            }

        } catch (IOException a) {
            LOGGER.catching(a);
        }


    }
}
/*JSON write
        JSONObject obj = new JSONObject();
        obj.put("name", "picture");
        obj.put("type", Entry.TYPE.SPRITE);
        obj.put("entryID", 0);
        obj.put("frame", 1);
        obj.put("framecount", 1);
        obj.put("offsetX", 0);
        obj.put("offsetY", 0);
        obj.put("boundwidth", 0);
        obj.put("boundheight", 0);

        try (FileWriter file = new FileWriter(infoFile.getAbsolutePath())) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

