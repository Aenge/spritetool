package com.SpriteTool.IO;

import com.SpriteTool.Model.Entry;

import com.SpriteTool.Model.Format.Info;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;

public class InfoReader {
    private Logger LOGGER = LogManager.getLogger();

    public Info read(File infoFile) {
        try {
            Info ret = new Info();
            if (infoFile.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jo = objectMapper.readTree(infoFile);

                ret.setType(Entry.TYPE.get(jo.get("type").asInt()));
                ret.setEntryID(jo.get("entryID").asInt());
                ret.setFrame(jo.get("frame").asInt());
                ret.setFrameCount(jo.get("framecount").asInt());
                ret.setOffsetX(jo.get("offsetX").asInt());
                ret.setOffsetY(jo.get("offsetY").asInt());
                ret.setBoundWidth(jo.get("boundwidth").asInt());
                ret.setBoundHeight(jo.get("boundheight").asInt());
                return ret;
            }

        } catch (IOException a) {
            LOGGER.catching(a);
        }

        return null;
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

